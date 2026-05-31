import MyCheckbox from "../components/shared/MyCheckbox";
import { FaTimes } from "react-icons/fa";

const STATUS_CONFIG = {
  UNASSIGNED: {
    bg: "bg-mint-soft",
    text: "text-mint-ink",
    dot: "bg-mint-ink",
    label: "Unassigned",
  },

  ASSIGNED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Assigned",
  },
};

const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? STATUS_CONFIG.UNASSIGNED;
  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] px-1.5 py-0.5 rounded font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};


const ShiftAssignmentsBody = ({ user, selectedUsers, setSelectedUsers, assignees, removeEmployee, shiftId }) => {

  const { id, firstName, lastName, weeklyHours, email } = user || {};
  const isAssigned = assignees.some(assignee => assignee.id === id);

  const toggleUser = (userId) => {
    setSelectedUsers((prev) =>
      prev.includes(userId)
        ? prev.filter((id) => id !== userId)
        : [...prev, userId]
    );
  };

  const handleRemoveEmployee = () => {
    removeEmployee(id, shiftId);
  }

  return (
    <tbody className="divide-y divide-ink-100">
      <tr
        key={id}
        className="hover:bg-ink-50/60 transition-colors"
      >
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
          <StatusBadge status={isAssigned ? "ASSIGNED" : "UNASSIGNED"} />
        </td>

        <td className="px-4 py-3 font-mono text-[12px] text-ink-700 text-center">
          <p className="justify-center">{weeklyHours ?? 0}h</p>
        </td>

        <td className="px-4 py-3 text-ink-500 text-[12px]">
          {email}
        </td>

        <td className="px-4 py-3">
          <div className="flex justify-center">
            <MyCheckbox
              checked={selectedUsers.includes(id)}
              onChange={() => toggleUser(id)}
              disabled={isAssigned}
            />
          </div>
        </td>

        {isAssigned &&
          <td className="px-4 py-3">
            <div className="flex justify-center">
              <button
                onClick={handleRemoveEmployee}
                className="w-8 h-8 flex items-center justify-center rounded-md bg-rose-soft text-rose-ink hover:bg-rose-soft/80"
                title="Reject"
              >
                <FaTimes size={12} />
              </button>
            </div>
          </td>
        }
      </tr>
    </tbody>
  )
}

export default ShiftAssignmentsBody
