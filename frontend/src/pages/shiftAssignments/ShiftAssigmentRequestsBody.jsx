import { IoIosSwap } from "react-icons/io";
import { Link } from "react-router";
import useAuthStore from "../../store/authStore";

import { FiUserMinus } from "react-icons/fi";

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

const ShiftAssignmentRequestsBody = ({ assignee, onRemove }) => {
  const role = useAuthStore((state) => state.user.role);
  const { firstName, lastName, weeklyHours, email, status, assigneeId } = assignee || {};

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
          {status === "ASSIGNED" && (
            <button
              onClick={() => onRemove(assigneeId, firstName, lastName)}
              className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 rounded-md text-rose-ink transition-colors"
            >
              <FiUserMinus size={13} />
              Remove
            </button>
          )}
        </td>

        <td className="px-4 py-3 flex justify-center">
          <Link to={`/${role}/shifts/${assigneeId}/reschedule-request`} state={{ assignee }}>
            <button
              className="inline-flex items-center gap-1 text-[12px] font-medium bg-mint-soft hover:bg-mint-soft/80 border border-mint-ink/20 px-2.5 py-1 rounded-md text-mint-ink transition-colors"
            >
              <IoIosSwap size={13} />
            </button>

          </Link>
        </td>
      </tr>
    </tbody>
  );
};

export default ShiftAssignmentRequestsBody
