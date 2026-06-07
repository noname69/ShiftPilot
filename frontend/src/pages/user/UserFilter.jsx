import { useState } from "react";

const STATUSES = [
  { label: "All", value: null },
  { label: "Active", value: "ACTIVE" },
  { label: "Inactive", value: "INACTIVE" },
  { label: "Absence", value: "ABSENCE" },
  { label: "Ill", value: "ILL" },
  { label: "Vacation", value: "VACATION" },
];

const ROLES = [
  { label: "All", value: null },
  { label: "Admin", value: "ADMIN" },
  { label: "Manager", value: "MANAGER" },
  { label: "User", value: "USER" },
];

const UserFilter = ({ filters, onFilterChange }) => {
  const [fullNameInput, setFullNameInput] = useState("");

  const handleSearch = () => {
    onFilterChange("searchByFullName", fullNameInput || null);
  };

  return (
    <div className="flex flex-wrap items-center gap-2.5 mb-4">

      <div className="inline-flex gap-0.5 bg-ink-50 p-0.5 rounded-lg border border-ink-200">
        {STATUSES.map(({ label, value }) => (
          <button
            key={label}
            onClick={() => onFilterChange("status", value)}
            className={`px-3 py-1.5 rounded-md text-[11.5px] font-medium uppercase tracking-wider transition-colors ${
              filters.status === value
                ? "bg-ink-900 text-white"
                : "bg-transparent text-ink-500 hover:text-ink-700"
            }`}
          >
            {label}
          </button>
        ))}
      </div>

      <div className="inline-flex gap-0.5 bg-ink-50 p-0.5 rounded-lg border border-ink-200">
        {ROLES.map(({ label, value }) => (
          <button
            key={label}
            onClick={() => onFilterChange("role", value)}
            className={`px-3 py-1.5 rounded-md text-[11.5px] font-medium uppercase tracking-wider transition-colors ${
              filters.role === value
                ? "bg-ink-900 text-white"
                : "bg-transparent text-ink-500 hover:text-ink-700"
            }`}
          >
            {label}
          </button>
        ))}
      </div>

      <div className="flex items-center gap-1.5 bg-ink-50 border border-ink-200 rounded-lg px-2.5 py-1.5">
        <input
          type="text"
          placeholder="Search by full name..."
          value={fullNameInput}
          onChange={(e) => setFullNameInput(e.target.value)}
          className="border-none bg-transparent text-[11.5px] font-medium uppercase tracking-wider text-ink-500 outline-none"
        />

        <button
          onClick={handleSearch}
          className="px-2 py-0.5 rounded-md text-[11.5px] font-medium uppercase tracking-wider bg-ink-900 text-white hover:bg-ink-800 transition-colors"
        >
          Search
        </button>
      </div>

    </div>
  );
};

export default UserFilter;