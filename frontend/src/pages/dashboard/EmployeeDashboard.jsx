import { useEffect, useState } from "react";
import useDashboardStore from "../../store/dashboardStore";
import Footer from "../components/shared/Footer";
import { formatTime } from "../../utils/formatDateTime";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";

const getWeekRange = (offset = 0) => {
  const today = new Date();
  const day = today.getDay();
  const diffToMonday = day === 0 ? -6 : 1 - day;
  const monday = new Date(today);
  monday.setDate(today.getDate() + diffToMonday + offset * 7);
  const sunday = new Date(monday);
  sunday.setDate(monday.getDate() + 6);
  const toISO = (d) => d.toISOString().split("T")[0];
  return { weekStart: toISO(monday), weekEnd: toISO(sunday) };
};

const formatWeekLabel = (weekStart, weekEnd) => {
  const opts = { month: "short", day: "numeric" };
  const s = new Date(weekStart + "T00:00:00").toLocaleDateString("en-US", opts);
  const e = new Date(weekEnd + "T00:00:00").toLocaleDateString("en-US", { ...opts, year: "numeric" });
  return `${s} – ${e}`;
};

const StatCard = ({ label, value, color }) => (
  <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
    <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-2">{label}</p>
    <p className={`text-[32px] font-semibold leading-none ${color ?? "text-ink-900"}`}>{value}</p>
  </div>
);

const EmployeeDashboard = () => {
  const [weekOffset, setWeekOffset] = useState(0);
  const { employeeData, isLoading, fetchEmployeeDashboard } = useDashboardStore((s) => s);

  const { weekStart, weekEnd } = getWeekRange(weekOffset);

  useEffect(() => {
    fetchEmployeeDashboard(weekStart, weekEnd);
  }, [weekOffset, fetchEmployeeDashboard, weekStart, weekEnd]);

  const shifts = employeeData?.upcomingShifts ?? [];
  const summary = employeeData?.requestSummary;

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="mb-6">
          <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">Dashboard</h1>
          <p className="text-[13px] text-ink-500 mt-0.5">Your schedule and requests</p>
        </div>

        {/* Stat cards */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">
          <StatCard label="Shifts This Week" value={shifts.length} />
          <StatCard label="Pending Requests" value={summary?.pendingCount ?? "—"} color="text-amber-ink" />
          <StatCard label="Approved Requests" value={summary?.approvedCount ?? "—"} color="text-mint-ink" />
        </div>

        {/* Week navigation + shifts table */}
        <div className="flex items-center justify-between mb-3">
          <h2 className="text-[13px] font-semibold text-ink-700 uppercase tracking-wider">
            Upcoming Shifts
          </h2>
          <div className="flex items-center gap-2">
            <button
              onClick={() => setWeekOffset((o) => o - 1)}
              className="w-7 h-7 flex items-center justify-center rounded-md border border-ink-200 text-ink-500 hover:bg-ink-100 transition"
            >
              <FaChevronLeft size={10} />
            </button>
            <span className="text-[12px] text-ink-600 font-mono min-w-36 text-center">
              {formatWeekLabel(weekStart, weekEnd)}
            </span>
            <button
              onClick={() => setWeekOffset((o) => o + 1)}
              className="w-7 h-7 flex items-center justify-center rounded-md border border-ink-200 text-ink-500 hover:bg-ink-100 transition"
            >
              <FaChevronRight size={10} />
            </button>
          </div>
        </div>

        <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft overflow-hidden">
          {isLoading ? (
            <p className="text-[13px] text-ink-400 px-4 py-6 text-center">Loading...</p>
          ) : shifts.length === 0 ? (
            <p className="text-[13px] text-ink-400 px-4 py-6 text-center">No shifts this week</p>
          ) : (
            <table className="w-full text-[13px]">
              <thead className="bg-ink-50 text-[11px] uppercase text-ink-500 border-b border-ink-200">
                <tr>
                  <th className="px-4 py-2.5 text-left font-medium">Title</th>
                  <th className="px-4 py-2.5 text-left font-medium">Date</th>
                  <th className="px-4 py-2.5 text-left font-medium">Time</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-ink-100">
                {shifts.map((s) => (
                  <tr key={s.shiftId} className="hover:bg-ink-50/40 transition">
                    <td className="px-4 py-3 font-medium text-ink-900">{s.title}</td>
                    <td className="px-4 py-3 text-ink-700 font-mono text-[12px]">
                      {new Date(s.shiftDate + "T00:00:00").toLocaleDateString("en-US", {
                        weekday: "short", month: "short", day: "numeric",
                      })}
                    </td>
                    <td className="px-4 py-3 text-ink-600 font-mono text-[12px]">
                      {formatTime(s.startTime)} – {formatTime(s.endTime)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default EmployeeDashboard;
