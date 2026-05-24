import { formatDate } from "../../utils/formatDateTime";
import { FaCheck, FaTimes } from "react-icons/fa";

const STATUS_CONFIG = {
  PENDING_TARGET_APPROVAL: {
    bg: "bg-amber-soft",
    text: "text-amber-ink",
    dot: "bg-amber-ink",
    label: "Waiting for target approval",
  },

  TARGET_REJECTED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Rejected by target user",
  },

  PENDING_MANAGER_APPROVAL: {
    bg: "bg-violet-soft",
    text: "text-violet-ink",
    dot: "bg-violet-ink",
    label: "Waiting for manager approval",
  },

  MANAGER_REJECTED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Rejected by manager",
  },

  COMPLETED: {
    bg: "bg-mint-soft",
    text: "text-mint-ink",
    dot: "bg-mint-ink",
    label: "Completed",
  },

  CANCELLED: {
    bg: "bg-ink-100",
    text: "text-ink-700",
    dot: "bg-ink-500",
    label: "Cancelled",
  },
};

const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? STATUS_CONFIG.PENDING_TARGET_APPROVAL;

  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] px-1.5 py-0.5 rounded font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};

const RequestRow = ({
  request,
  onTargetRespond,
  onManagerRespond,
  currentUserId,
  isManager,
}) => {
  const {
    id,
    requester,
    targetUser,
    requesterShift,
    targetShift,
    status,
    reason,
    createdAt,
  } = request;

  console.log(createdAt);

  const isTargetUser = currentUserId === targetUser?.id;

  console.log(currentUserId, targetUser?.id, isTargetUser);

  const canTargetRespond = isTargetUser && status === "PENDING_TARGET_APPROVAL";

  const canManagerRespond = isManager && status === "PENDING_MANAGER_APPROVAL";

  return (
    <tr className="hover:bg-ink-50/60 transition-colors">
      {/* REQUESTER */}
      <td className="px-4 py-3">
        <div className="font-medium text-ink-900">
          {requester?.firstName} {requester?.lastName}
        </div>
        <div className="text-[11.5px] text-ink-500">{requester?.email}</div>
      </td>

      {/* TARGET */}
      <td className="px-4 py-3">
        <div className="font-medium text-ink-900">
          {targetUser?.firstName} {targetUser?.lastName}
        </div>
        <div className="text-[11.5px] text-ink-500">{targetUser?.email}</div>
      </td>

      {/* SHIFT SWAP */}
      <td className="px-4 py-3 text-[12px] text-ink-700">
        <div className="space-y-2">
          {/* REQUESTER SHIFT */}
          <div>
            <div className="font-medium text-ink-900">
              {requesterShift?.title}
            </div>
            <div className="text-[11px] text-ink-500 font-mono">
              {requesterShift?.shiftDate} ·{" "}
              {requesterShift?.startTime?.slice(0, 5)} -{" "}
              {requesterShift?.endTime?.slice(0, 5)}
            </div>
          </div>

          {/* ARROW */}
          <div className="text-ink-400 text-[11px]">↕ swap ↕</div>

          {/* TARGET SHIFT */}
          <div>
            <div className="font-medium text-ink-900">{targetShift?.title}</div>
            <div className="text-[11px] text-ink-500 font-mono">
              {targetShift?.shiftDate} · {targetShift?.startTime?.slice(0, 5)} -{" "}
              {targetShift?.endTime?.slice(0, 5)}
            </div>
          </div>
        </div>
      </td>

      {/* REASON */}
      <td className="px-4 py-3 text-ink-600 max-w-[260px] truncate">
        {reason || "—"}
      </td>

      {/* STATUS */}
      <td className="px-4 py-3">
        <StatusBadge status={status} />
      </td>

      {/* CREATED */}
      <td className="px-4 py-3 text-[12px] text-ink-500 font-mono">
        {formatDate(createdAt)}
      </td>

      {/* ACTIONS */}
      <td className="px-4 py-3 text-right">
        <div className="inline-flex gap-1.5">
          {/* TARGET USER ACTIONS */}
          {canTargetRespond && (
            <>
              <button
                onClick={() => onTargetRespond(id, true)}
                className="inline-flex items-center gap-1 text-[12px] font-medium bg-mint-ink text-white px-2.5 py-1 rounded-md hover:bg-mint-ink/90"
              >
                <FaCheck size={11} />
              </button>

              <button
                onClick={() => onTargetRespond(id, false)}
                className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft text-rose-ink border border-rose-ink/20 px-2.5 py-1 rounded-md hover:bg-rose-soft/80"
              >
                <FaTimes size={11} />
              </button>
            </>
          )}

          {/* MANAGER ACTIONS */}
          {canManagerRespond && (
            <>
              <button
                onClick={() => onManagerRespond(id, true)}
                className="inline-flex items-center gap-1 text-[12px] font-medium bg-mint-ink text-white px-2.5 py-1 rounded-md hover:bg-mint-ink/90"
              >
                <FaCheck size={11} />
              </button>

              <button
                onClick={() => onManagerRespond(id, false)}
                className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft text-rose-ink border border-rose-ink/20 px-2.5 py-1 rounded-md hover:bg-rose-soft/80"
              >
                <FaTimes size={11} />
              </button>
            </>
          )}

          {/* NO ACTION */}
          {!canTargetRespond && !canManagerRespond && (
            <span className="text-[12px] text-ink-400">—</span>
          )}
        </div>
      </td>
    </tr>
  );
};

export default RequestRow;
