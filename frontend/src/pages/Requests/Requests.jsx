import { useEffect } from "react";
// import useSwapRequestStore from "../../store/swapStore";
import RequestRow from "./RequestRow";
import useAuthStore from "../../store/authStore";
import useManagerApprovalsStore from "../../store/managerApprovalsStore";

const Requests = () => {

  // 

  const { fetchManagerApprovals, requests } = useManagerApprovalsStore(state => state);

  // 

  // const {
  //   requests,
  //   fetchMyRequests,
  //   fetchAllRequests,
  //   respondAsTarget,
  //   respondAsManager,
  // } = useSwapRequestStore((state) => state);

  const { user } = useAuthStore((state) => state);

  const userId = user?.userId;
  const role = user?.role;

  const isManager = role === "MANAGER";
  const isAdmin = role === "ADMIN";

  const loadRequests = () => {
    fetchManagerApprovals();
    // return role === "MANAGER" || role === "ADMIN"
    //   ? fetchAllRequests()
    //   : fetchMyRequests();
  };

  useEffect(() => {
    loadRequests();
  }, [role]);


  console.log(requests)
  return (
    <div className="px-5 lg:px-8 py-7 max-w-[1300px] mx-auto">
      <h1 className="text-[32px] font-serif text-ink-900 mb-6">Requests</h1>

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
            {(requests ?? []).map((r) => (
              <RequestRow
                key={r.requestId}
                request={r}
                currentUserId={userId}
                isManager={isManager}
                isAdmin={isAdmin}
                // onTargetRespond={respondAsTarget}
                // onManagerRespond={respondAsManager}
              />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Requests;
