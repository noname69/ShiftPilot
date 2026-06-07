import { FiChevronLeft, FiChevronRight } from "react-icons/fi";

const Pagination = ({ totalPages, currentPage, setPage }) => {
  return (
    <>
      {totalPages > 1 && (
        <div className="flex items-center justify-between px-4 py-3 border-t border-ink-200">
          <p className="text-[13px] text-ink-500">
            Page {currentPage + 1} of {totalPages}
          </p>
          <div className="flex items-center gap-1">
            <button
              onClick={() => setPage((p) => p - 1)}
              disabled={currentPage === 0}
              className="px-2.5 py-1 rounded-md text-[13px] border border-ink-200 text-ink-600 hover:bg-ink-50 disabled:opacity-40 disabled:cursor-not-allowed"
            >
              <FiChevronLeft size={14} />
            </button>
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i}
                onClick={() => setPage(i)}
                className={`px-2.5 py-1 rounded-md text-[13px] border ${
                  currentPage === i
                    ? "bg-ink-900 text-white border-ink-900"
                    : "border-ink-200 text-ink-600 hover:bg-ink-50"
                }`}
              >
                {i + 1}
              </button>
            ))}
            <button
              onClick={() => setPage((p) => p + 1)}
              disabled={currentPage === totalPages - 1}
              className="px-2.5 py-1 rounded-md text-[13px] border border-ink-200 text-ink-600 hover:bg-ink-50 disabled:opacity-40 disabled:cursor-not-allowed"
            >
              <FiChevronRight size={14} />
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default Pagination;
