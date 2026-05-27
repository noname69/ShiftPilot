import { formatDate } from "../../utils/formatDateTime";
import { FaCheck, FaTimes } from "react-icons/fa";

const STATUS_CONFIG = {
  PENDING_TARGET_APPROVAL: {
    bg: "bg-amber-soft",
    text: "text-amber-ink",
    dot: "bg-amber-ink",
    label: "Target approval",
  },
  TARGET_REJECTED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Target rejected",
  },
  PENDING_MANAGER_APPROVAL: {
    bg: "bg-violet-soft",
    text: "text-violet-ink",
    dot: "bg-violet-ink",
    label: "Manager approval",
  },
  MANAGER_REJECTED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Manager rejected",
  },
  APPROVED: {
    bg: "bg-mint-soft",
    text: "text-mint-ink",
    dot: "bg-mint-ink",
    label: "Approved",
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
      className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[11px] font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};

const UserCard = ({ user, tone }) => {
  const tones = {
    requester: "bg-mint-soft text-mint-ink border-mint-ink/20",
    target: "bg-violet-soft text-violet-ink border-violet-ink/20",
  };

  const initials = (user?.firstName?.[0] ?? "") + (user?.lastName?.[0] ?? "");

  return (
    <div
      className={`flex items-center gap-2 px-3 py-2 rounded-md border ${
        tones[tone] ?? "bg-ink-50 text-ink-900 border-ink-200"
      }`}
    >
      <div className="w-7 h-7 rounded-full bg-white/70 flex items-center justify-center text-[11px] font-semibold">
        {initials}
      </div>

      <div className="min-w-0">
        <div className="text-[12px] font-medium truncate">
          {user?.firstName} {user?.lastName}
        </div>
        <div className="text-[10.5px] opacity-70 truncate">{user?.email}</div>
      </div>
    </div>
  );
};

const ShiftCard = ({ title, date, start, end }) => (
  <div className="rounded-md border border-ink-200 bg-white px-3 py-2">
    <div className="text-[12px] font-medium text-ink-900">{title}</div>
    <div className="text-[11px] text-ink-500 font-mono">
      {date} · {start} → {end}
    </div>
  </div>
);

const RequestRow = ({
  request,
  onTargetRespond,
  onManagerRespond,
  currentUserId,
  isManager,
  isUser,
}) => {
  const { requestId, approvalStatus, reason, createdAt } = request;

  let requester = {};
  let targetUser = {};
  let requesterShift = {};
  let targetShift = {};

  const isSwapRequest = request.swapResponse !== null;

  if (isSwapRequest) {
    requester = request.swapResponse.requester;
    requesterShift = request.swapResponse.requesterShift;
    targetUser = request.swapResponse.targetUser;
    targetShift = request.swapResponse.targetShift;
  } else if (!isSwapRequest) {
    requester = request.leaveResponse.requester;
    requesterShift = request.leaveResponse.requesterShift;
  }

  // const isTargetUser = currentUserId === targetUser?.id;

  // const canTargetRespond =
  //   isTargetUser && status === "PENDING_TARGET_APPROVAL";

  // const canManagerRespond =
  //   isManager && status === "PENDING_MANAGER_APPROVAL";

  // const canAdminRespond =
  //   isAdmin && status === "PENDING_MANAGER_APPROVAL";

  const time = (t) => (t ? t.slice(0, 5) : "");

  return (
    <tr className="border-t border-ink-100 hover:bg-ink-50/40 transition">
      {/* USERS SWAP */}
      <td className="px-4 py-3 align-top min-w-65">
        <UserCard user={requester} tone="requester" />

        {isSwapRequest && (
          <>
            <div className="flex items-center justify-center text-[11px] text-ink-400 my-2">
              ⇄ swap request ⇄
            </div>

            <UserCard user={targetUser} tone="target" />
          </>
        )}
      </td>

      {/* SHIFTS */}
      <td className="px-4 py-3 align-top min-w-65">
        <ShiftCard
          title={requesterShift?.title}
          date={requesterShift?.shiftDate}
          start={time(requesterShift?.startTime)}
          end={time(requesterShift?.endTime)}
        />

        {isSwapRequest && (
          <>
            <div className="flex items-center justify-center text-[11px] text-ink-400 my-2">
              ↕ shift swap ↕
            </div>

            <ShiftCard
              title={targetShift?.title}
              date={targetShift?.shiftDate}
              start={time(targetShift?.startTime)}
              end={time(targetShift?.endTime)}
            />
          </>
        )}
      </td>

      {/* REASON */}
      <td className="px-4 py-3 text-[12px] text-ink-600 max-w-55">
        <div className="line-clamp-3">{reason || "—"}</div>
      </td>

      {/* STATUS */}
      <td className="px-4 py-3">
        <StatusBadge status={approvalStatus} />
      </td>

      {/* CREATED */}
      <td className="px-4 py-3 text-[12px] text-ink-500 font-mono">
        {formatDate(createdAt)}
      </td>

      {/* ACTIONS */}
      <td className="px-4 py-3 text-right">
        <div className="flex justify-end gap-2">
          {/* TARGET */}
          {isUser &&
            targetUser.id === currentUserId &&
            approvalStatus === "PENDING_TARGET_APPROVAL" && (
              <>
                <button
                  onClick={() => onTargetRespond(requestId, true)}
                  className="w-8 h-8 flex items-center justify-center rounded-md bg-mint-soft text-mint-ink hover:bg-mint-soft/80"
                  title="Accept"
                >
                  <FaCheck size={12} />
                </button>

                <button
                  onClick={() => onTargetRespond(requestId, false)}
                  className="w-8 h-8 flex items-center justify-center rounded-md bg-rose-soft text-rose-ink hover:bg-rose-soft/80"
                  title="Decline"
                >
                  <FaTimes size={12} />
                </button>
              </>
            )}

          {/* MANAGER */}
          {isManager && approvalStatus === "PENDING_MANAGER_APPROVAL" && (
            <>
              <button
                onClick={() => onManagerRespond(requestId, true)}
                className="w-8 h-8 flex items-center justify-center rounded-md bg-mint-ink text-white hover:bg-mint-ink/90"
                title="Approve"
              >
                <FaCheck size={12} />
              </button>

              <button
                onClick={() => onManagerRespond(requestId, false)}
                className="w-8 h-8 flex items-center justify-center rounded-md bg-rose-soft text-rose-ink hover:bg-rose-soft/80"
                title="Reject"
              >
                <FaTimes size={12} />
              </button>
            </>
          )}

          {!isUser && !isManager && (
            <span className="text-[12px] text-ink-400">—</span>
          )}
        </div>
      </td>
    </tr>
  );
};

export default RequestRow;
