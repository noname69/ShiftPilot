const SHIFT_STATUSES = [
  { label: "All", value: null },
  { label: "Open", value: "OPEN" },
  { label: "Ongoing", value: "ONGOING" },
  { label: "Completed", value: "COMPLETED" },
  { label: "Cancelled", value: "CANCELLED" },
];

const MyScheduleFilter = ({ filters, onFilterChange }) => {
  return (
    <div className="flex flex-wrap items-center gap-2.5 mb-4">
      <div className="inline-flex gap-0.5 bg-ink-50 p-0.5 rounded-lg border border-ink-200">
        {SHIFT_STATUSES.map(({ label, value }) => (
          <button
            key={label}
            onClick={() => onFilterChange("shiftStatus", value)}
            className={`px-3 py-1.5 rounded-md text-[11.5px] font-medium uppercase tracking-wider transition-colors ${
              filters.shiftStatus === value
                ? "bg-ink-900 text-white"
                : "bg-transparent text-ink-500 hover:text-ink-700"
            }`}
          >
            {label}
          </button>
        ))}
      </div>

      <div className="flex items-center gap-1.5 bg-ink-50 border border-ink-200 rounded-lg px-2.5 py-1.5">
        <span className="text-ink-400 text-[13px]">📅</span>

        <input
          type="date"
          value={filters.shiftDate ?? ""}
          onChange={(e) =>
            onFilterChange("shiftDate", e.target.value || null)
          }
          className="border-none bg-transparent text-[11.5px] font-medium uppercase tracking-wider text-ink-500 outline-none cursor-pointer"
        />

        {filters.shiftDate && (
          <>
            <span className="text-ink-300 text-[11px]">|</span>

            <button
              onClick={() => onFilterChange("shiftDate", null)}
              className="text-[11px] font-medium uppercase tracking-wider text-ink-500 hover:text-ink-700"
            >
              Clear
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default MyScheduleFilter;