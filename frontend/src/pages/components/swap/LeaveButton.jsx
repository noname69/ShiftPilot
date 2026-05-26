import { FaUserClock } from "react-icons/fa";

const LeaveButton = ({ onClick, disabled = false }) => {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className="flex items-center gap-2 px-4 py-2 rounded-lg bg-violet-600 text-white hover:bg-violet-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-[13px] font-medium"
    >
      <FaUserClock size={14} />
      Time Off
    </button>
  );
};

export default LeaveButton;
