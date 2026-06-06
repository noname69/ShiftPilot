import { formatTime } from "../../utils/formatDateTime";
import { FiTrash2 } from "react-icons/fi";
import useShiftDraftsStore from "../../store/shiftDraftsStore";
import useAuthStore from "../../store/authStore";
import { useNavigate } from "react-router";

const ShiftDraftsRow = ({ draft }) => {
  const {
    id,
    title,
    description,
    endTime,
    startTime,
    minEmployees,
    draftEmployees,
  } = draft;

  const { removeDraft } = useShiftDraftsStore();
  const role = useAuthStore(state => state.user.role)
  const navigate = useNavigate();

  const handleRemoveDraft = (draftId) => {
    removeDraft(draftId, navigate, role);
  }

  return (
    <>
      <tr key={id} className="hover:bg-ink-50/60 transition-colors">
        <td className="px-4 py-3 font-medium text-ink-900">{title}</td>
        <td className="px-4 py-3 text-ink-600 max-w-50 truncate hidden md:table-cell">
          {description}
        </td>
        <td className="px-4 py-3 text-ink-600 font-mono text-[12px] hidden sm:table-cell">
          {formatTime(startTime)} – {formatTime(endTime)}
        </td>
        <td className="px-4 py-3 text-ink-700 hidden lg:table-cell">
          {minEmployees}
        </td>
        <td className="px-4 py-3 text-ink-700 hidden lg:table-cell text-center">
          {draftEmployees?.length}
        </td>
        <td className="flex justify-center p-2">
          <button
            onClick={() => handleRemoveDraft(id)}
            className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 rounded-md text-rose-ink transition-colors"
          >
            <FiTrash2 size={13} />
          </button>
        </td>
      </tr>
    </>
  );
};

export default ShiftDraftsRow;
