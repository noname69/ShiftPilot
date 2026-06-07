import { useEffect, useState } from "react";
import useSwapRequestStore from "../../store/swapStore";
import RequestRow from "./RequestRow";
import useAuthStore from "../../store/authStore";
import useManagerApprovalsStore from "../../store/managerApprovalsStore";
import RequestFilter from "./RequestFilter";
import Pagination from "../components/shared/Pagination";

const Requests = ({ isManager = false, isUser = false }) => {
  const {
    fetchManagerApprovals,
    fetchUserRequests,
    requests,
    processRequest,
    totalPages,
    currentPage,
  } = useManagerApprovalsStore((state) => state);
  const { sendTargetResponse } = useSwapRequestStore((state) => state);

  const { user } = useAuthStore((state) => state);

  const userId = user?.userId;

  const [filters, setFilters] = useState({
    status: null,
    createdFrom: null,
    createdTo: null,
    requester: "",
  });

  const [page, setPage] = useState(0);

  const handleFilterChange = (key, value) => {
    setFilters((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  useEffect(() => {
    if (isManager) {
      fetchManagerApprovals({ ...filters, page: page });
    } else if (isUser) {
      fetchUserRequests({ ...filters, page: page });
    }
  }, [
    isManager,
    isUser,
    fetchManagerApprovals,
    fetchUserRequests,
    filters,
    page,
  ]);

  const handleTargetRespond = async (requestId, decision) => {
    console.log("TARGET RESPOND PAYLOAD:", requestId, decision);

    try {
      const res = await sendTargetResponse({
        swapRequestId: requestId,
        accepted: decision,
        comment: "",
      });

      console.log("TARGET RESPOND RESULT:", res);

      if (isUser) {
        await fetchUserRequests();
      } else if (isManager) {
        await fetchManagerApprovals();
      }

      return res;
    } catch (err) {
      console.error("TARGET RESPOND ERROR:", err);
      throw err;
    }
  };

  const handleManagerRespond = async (
    requestId,
    approvalId,
    requestType,
    decision,
  ) => {
    console.log("MANAGER RESPOND PAYLOAD:", {
      approvalId: approvalId,
      requestId: requestId,
      requestType: requestType,
      approved: decision,
      comment: "",
    });

    try {
      // const res = await sendManagerResponse({
      //   swapRequestId: requestId,
      //   approved: decision,
      //   comment: "",
      // });

      const res = await processRequest({
        approvalId: approvalId,
        requestId: requestId,
        requestType: requestType,
        decision: decision,
        // "comment" : ""
      });

      if (isManager) {
        await fetchManagerApprovals();
      }

      return res;
    } catch (err) {
      console.error("MANAGER RESPOND ERROR:", err);
      throw err;
    }
  };

  return (
    <div className="flex flex-col">
      <RequestFilter
        filters={filters}
        onFilterChange={handleFilterChange}
        isUser={isUser}
      />
      <div className="bg-white border border-ink-200 rounded-xl2 overflow-hidden">
        <table className="w-full text-[13px]">
          <thead className="bg-ink-50 text-[11px] uppercase text-ink-500">
            <tr>
              <th className="px-4 py-2 text-left">Requester / Target</th>

              <th className="px-4 py-2 text-left">Swap</th>
              <th className="px-4 py-2 text-left">Reason</th>
              <th className="px-4 py-2 text-left">Status</th>
              <th className="px-4 py-2 text-left">Created</th>
              <th className="px-4 py-2 text-right">Actions</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-ink-100">
            {(requests ?? []).map((r, index) => (
              <RequestRow
                key={index}
                request={r}
                currentUserId={userId}
                isManager={isManager}
                isUser={isUser}
                onTargetRespond={handleTargetRespond}
                onManagerRespond={handleManagerRespond}
              />
            ))}
          </tbody>
        </table>
        <Pagination
          totalPages={totalPages}
          currentPage={currentPage}
          setPage={setPage}
        />
      </div>
    </div>
  );
};

export default Requests;
