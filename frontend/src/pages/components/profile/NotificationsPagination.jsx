import { FiChevronLeft, FiChevronRight } from "react-icons/fi";

const NotificationsPagination = ({ totalPages, currentPage, setPage }) => {
  if (totalPages <= 1) return null;

  return (
    <div className="flex items-center justify-end gap-1 px-4 py-3 border-t border-ink-200">
      <button
        onClick={() => setPage((prev) => prev - 1)}
        disabled={currentPage === 0}
        className="px-2.5 py-1 rounded-md border border-ink-200 text-ink-600 hover:bg-ink-50 disabled:opacity-40 disabled:cursor-not-allowed"
      >
        <FiChevronLeft size={14} />
      </button>

      <button
        onClick={() => setPage((prev) => prev + 1)}
        disabled={currentPage === totalPages - 1}
        className="px-2.5 py-1 rounded-md border border-ink-200 text-ink-600 hover:bg-ink-50 disabled:opacity-40 disabled:cursor-not-allowed"
      >
        <FiChevronRight size={14} />
      </button>
    </div>
  );
};

export default NotificationsPagination;