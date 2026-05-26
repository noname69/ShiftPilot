import { useEffect } from "react";
import useSwapRequestStore from "../../store/swapStore";
import RequestRow from "./RequestRow";
import useAuthStore from "../../store/authStore";
import useManagerApprovalsStore from "../../store/managerApprovalsStore";

const Requests = ({ isManager = false, isUser = false }) => {

  const { fetchManagerApprovals, fetchUserRequests, requests } = useManagerApprovalsStore((state) => state);
  const { respondAsTarget, respondAsManager } = useSwapRequestStore((state) => state);

  const { user } = useAuthStore((state) => state);

  const userId = user?.userId;

  useEffect(() => {
    if(isManager) {
      fetchManagerApprovals();
    } else if(isUser) {
      fetchUserRequests();
    }
  }, [isManager, isUser, fetchManagerApprovals, fetchUserRequests]);

  return (
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
                isUser={isUser}
                // isAdmin={isAdmin}
                onTargetRespond={respondAsTarget}
                onManagerRespond={respondAsManager}
              />
            ))}
          </tbody>
        </table>
      </div>
  );
};

export default Requests;
