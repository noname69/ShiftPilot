import { formatDate, formatTime } from "../../utils/formatDateTime";
import { useState } from "react";

import useRescheduleStore from "../../store/rescheduleStore";

const STATUS_CONFIG = {
  OPEN: {
    bg: "bg-mint-soft",
    text: "text-mint-ink",
    dot: "bg-mint-ink",
    label: "Open",
  },
  ONGOING: {
    bg: "bg-amber-soft",
    text: "text-amber-ink",
    dot: "bg-amber-ink",
    label: "Ongoing",
  },
  COMPLETED: {
    bg: "bg-ink-100",
    text: "text-ink-600",
    dot: "bg-ink-400",
    label: "Completed",
  },
  CANCELLED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Cancelled",
  },
};

const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? STATUS_CONFIG.OPEN;
  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] px-1.5 py-0.5 rounded font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};

const MyScheduleBody = ({ shift, isRescheduleMode, selectedShiftId, }) => {

  const { id, 
    title, 
    description, 
    shiftDate, 
    startTime, 
    endTime, minEmployees, status, createdByUsername, assigneeId } = shift;

  const [openSwap, setOpenSwap] = useState(false);
  const [reason, setReason] = useState("");

  const { sendRescheduleRequest, isLoading } = useRescheduleStore(
    (state) => state,
  );

  const handleSend = async () => {
    if (!selectedShiftId) return;
    if (!reason.trim()) return;

    const payload = {
      requesterAssignmentId: assigneeId,
      targetAssignmentId: Number(selectedShiftId),
      reason: reason.trim(),
    };

    console.log("reschedule request", payload);

    const result = await sendRescheduleRequest(payload);

    if (result.success) {
      setOpenSwap(false);
      setReason("");

      console.log("Request sent");
    } else {
      console.error(result.message);
    }
  };

  return (
      <tbody className="divide-y divide-ink-100">
        <tr
          key={id}
          className="hover:bg-ink-50/60 transition-colors"
        >
          <td className="px-4 py-3 font-medium text-ink-900">
            {title}
          </td>
          <td className="px-4 py-3 text-ink-600 max-w-50 truncate hidden md:table-cell">
            {description}
          </td>
          <td className="px-4 py-3 text-ink-700 font-mono text-[12px]">
            {formatDate(shiftDate)}
          </td>
          <td className="px-4 py-3 text-ink-600 font-mono text-[12px] hidden sm:table-cell">
            {formatTime(startTime)} –{" "}
            {formatTime(endTime)}
          </td>
          <td className="px-4 py-3 text-ink-700 hidden lg:table-cell text-center">
            {minEmployees}
          </td>
          <td className="px-4 py-3 text-center">
            <StatusBadge status={status} />
          </td>
          <td className="px-4 py-3 text-ink-500 hidden xl:table-cell text-center">
            {createdByUsername}
          </td>
          <td className="px-4 py-3 text-right">
  <div className="inline-flex flex-col gap-2 items-end">

    {!isRescheduleMode ? (
      <span className="text-ink-400 text-[12px]"></span>
    ) : !openSwap ? (
      <button
        onClick={() => setOpenSwap(true)}
        className="inline-flex items-center text-[12px] font-medium bg-mint-soft hover:bg-mint-soft/80 border border-mint-ink/20 px-2.5 py-1 rounded-md text-mint-ink transition-colors"
      >
        Send
      </button>
    ) : (
      <div className="flex flex-col gap-2 w-60">
        <textarea
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          placeholder="Reason for swap..."
          className="w-full text-[12px] border border-ink-200 rounded-md p-2 resize-none focus:outline-none focus:ring-1 focus:ring-mint-ink"
          rows={2}
        />

        <div className="flex justify-end gap-2">
          <button
            onClick={() => {
              setOpenSwap(false);
              setReason("");
            }}
            className="text-[12px] px-2 py-1 text-ink-500"
          >
            Cancel
          </button>

          <button
            onClick={handleSend}
            className="text-[12px] px-2 py-1 rounded-md bg-mint-ink text-white hover:bg-mint-ink/90"
          >
            Confirm
          </button>
        </div>
      </div>
    )}
  </div>
</td>
        </tr>
      </tbody>
  )
}

export default MyScheduleBody
