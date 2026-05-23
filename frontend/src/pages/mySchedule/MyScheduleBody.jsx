import { formatDate, formatTime } from "../../utils/formatDateTime";

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

const MyScheduleBody = ({ shift }) => {

  const { id, title, description, shiftDate, startTime, endTime, minEmployees, status, createdByUsername } = shift;

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
            <div className="inline-flex gap-1.5">
            </div>
          </td>
        </tr>
      </tbody>
  )
}

export default MyScheduleBody
