import { IoIosSwap } from "react-icons/io";
import { FiUserMinus } from "react-icons/fi";

import useAuthStore from "../../store/authStore";

const STATUS_CONFIG = {
  ASSIGNED: {
    bg: "bg-amber-soft",
    text: "text-amber-ink",
    dot: "bg-amber-ink",
    label: "Assigned",
  },
  CONFIRMED: {
    bg: "bg-green-50",
    text: "text-green-800",
    dot: "bg-green-600",
    label: "Confirmed",
  },
  REMOVED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Removed",
  },
};

const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? STATUS_CONFIG.ASSIGNED;

  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] px-1.5 py-0.5 rounded font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};

const ShiftAssignmentRequestsBody = ({ assignee, onRemove, onOpenSwap }) => {
  const role = useAuthStore((state) => state.user.role);
  const { id, firstName, lastName, weeklyHours, email, status, assigneeId } =
    assignee || {};
  const userId = useAuthStore((state) => state.user.userId);
  const isCurrentUser = userId === id;

  return (
    <tbody className="divide-y divide-ink-100">
      <tr className="hover:bg-ink-50/60 transition-colors">
        <td className="px-4 py-3">
          <div className="flex items-center gap-2.5">
            <div className="w-7 h-7 rounded-full bg-violet-soft text-violet-ink text-[11px] font-semibold flex items-center justify-center">
              {firstName?.[0]}
              {lastName?.[0]}
            </div>

            <div className="font-medium text-ink-900">
              {firstName} {lastName}
            </div>
          </div>
        </td>

        <td className="px-4 py-3 text-center">
          <StatusBadge status={status} />
        </td>

        <td className="px-4 py-3 font-mono text-[12px] text-ink-700 text-center">
          <p className="justify-center">{weeklyHours ?? 0}h</p>
        </td>

        <td className="px-4 py-3 text-ink-500 text-[12px]">{email}</td>

        <td className="px-4 py-3 flex justify-center">
          <div className="flex justify-end gap-2">
            {status === "ASSIGNED" && (
              <>
                <button
                  disabled={isCurrentUser}
                  onClick={() => {
                    if (!isCurrentUser) {
                      onOpenSwap(assigneeId);
                    }
                  }}
                  className={`w-8 h-8 flex items-center justify-center rounded-md transition-colors
                  ${isCurrentUser
                      ? "bg-ink-100 text-ink-400 cursor-not-allowed"
                      : "bg-violet-soft text-violet-ink hover:bg-violet-soft/80"
                    }`}
                  title={
                    isCurrentUser
                      ? "You cannot swap with yourself"
                      : "Request swap"
                  }
                >
                  <IoIosSwap size={12} />
                </button>
                {(role === "MANAGER" || role === "ADMIN") && (
                  <button
                    onClick={() => onRemove(assigneeId, firstName, lastName)}
                    className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 rounded-md text-rose-ink transition-colors"
                  >
                    <FiUserMinus size={13} />
                  </button>
                )}
              </>
            )}
          </div>
        </td>
      </tr>
    </tbody>
  );
};

export default ShiftAssignmentRequestsBody;
