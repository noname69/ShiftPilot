import { useEffect, useState } from "react";
import useDashboardStore from "../../store/dashboardStore";
import Footer from "../components/shared/Footer";
import { formatTime, formatDate } from "../../utils/formatDateTime";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import { respondAsSwapTarget } from "../../api/dashboard";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell,
  PieChart, Pie,
} from "recharts";

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

const DAY_ORDER = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

const computeDayHours = (shifts) => {
  const acc = Object.fromEntries(DAY_ORDER.map((d) => [d, 0]));
  shifts.forEach((s) => {
    const day = new Date(s.shiftDate + "T00:00:00").toLocaleDateString("en-US", { weekday: "short" });
    const [sh, sm] = s.startTime.split(":").map(Number);
    const [eh, em] = s.endTime.split(":").map(Number);
    const h = (eh * 60 + em - (sh * 60 + sm)) / 60;
    if (acc[day] !== undefined) acc[day] += h;
  });
  return DAY_ORDER.map((name) => ({ name, hours: parseFloat(acc[name].toFixed(1)) }));
};

const WeeklyHoursChart = ({ upcomingShifts, completedShifts }) => {
  const data = computeDayHours([...upcomingShifts, ...completedShifts]);
  return (
    <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
      <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-4">Hours per Day</p>
      <ResponsiveContainer width="100%" height={120}>
        <BarChart data={data} barCategoryGap="35%">
          <XAxis dataKey="name" tick={{ fontSize: 11, fill: "#6B6B66" }} axisLine={false} tickLine={false} />
          <YAxis hide />
          <Tooltip
            cursor={{ fill: "#F4F4F2" }}
            content={({ active, payload, label }) =>
              active && payload?.length ? (
                <div className="bg-white border border-ink-200 rounded-lg px-2.5 py-1.5 text-[12px] shadow-soft">
                  <span className="text-ink-500">{label} </span>
                  <span className="font-semibold text-ink-900">{payload[0].value}h</span>
                </div>
              ) : null
            }
          />
          <Bar dataKey="hours" radius={[3, 3, 0, 0]}>
            {data.map((entry, i) => (
              <Cell key={i} fill={entry.hours > 0 ? "#0F0F10" : "#E8E8E4"} />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

const RequestStatusChart = ({ summary }) => {
  const total = (summary?.pendingCount ?? 0) + (summary?.approvedCount ?? 0) + (summary?.rejectedCount ?? 0);
  if (!total) return null;
  const data = [
    { name: "Pending", value: summary.pendingCount, color: "#7A5712" },
    { name: "Approved", value: summary.approvedCount, color: "#1F6B3A" },
    { name: "Rejected", value: summary.rejectedCount, color: "#923232" },
  ].filter((d) => d.value > 0);
  return (
    <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
      <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-4">Request Status</p>
      <div className="flex items-center gap-5">
        <div className="relative flex-shrink-0 w-[100px] h-[100px]">
          <PieChart width={100} height={100}>
            <Pie data={data} cx="50%" cy="50%" innerRadius={28} outerRadius={44} dataKey="value" strokeWidth={0}>
              {data.map((entry, i) => (
                <Cell key={i} fill={entry.color} />
              ))}
            </Pie>
          </PieChart>
          <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
            <span className="text-[16px] font-semibold text-ink-900">{total}</span>
          </div>
        </div>
        <div className="flex flex-col gap-2">
          {data.map((d) => (
            <div key={d.name} className="flex items-center gap-2 text-[12px]">
              <span className="w-2 h-2 rounded-sm flex-shrink-0" style={{ backgroundColor: d.color }} />
              <span className="text-ink-500">{d.name}</span>
              <span className="font-semibold text-ink-900 ml-auto pl-3">{d.value}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

const StatCard = ({ label, value, color }) => (
  <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
    <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-2">{label}</p>
    <p className={`text-[32px] font-semibold leading-none ${color ?? "text-ink-900"}`}>{value}</p>
  </div>
);

const ShiftTable = ({ shifts, emptyLabel }) => (
  <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft overflow-hidden">
    {shifts.length === 0 ? (
      <p className="text-[13px] text-ink-400 px-4 py-6 text-center">{emptyLabel}</p>
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
);

const EmployeeDashboard = () => {
  const [weekOffset, setWeekOffset] = useState(0);
  const [actionLoading, setActionLoading] = useState(null);
  const { employeeData, isLoading, fetchEmployeeDashboard } = useDashboardStore((s) => s);

  const { weekStart, weekEnd } = getWeekRange(weekOffset);

  useEffect(() => {
    fetchEmployeeDashboard(weekStart, weekEnd);
  }, [weekOffset, fetchEmployeeDashboard, weekStart, weekEnd]);

  const handleTargetDecision = async (r, accepted) => {
    setActionLoading(r.approvalId);
    try {
      await respondAsSwapTarget(r.requestId, accepted);
      fetchEmployeeDashboard(weekStart, weekEnd);
    } catch (e) {
      console.error(e);
    } finally {
      setActionLoading(null);
    }
  };

  const upcomingShifts = employeeData?.upcomingShifts ?? [];
  const completedShifts = employeeData?.completedShifts ?? [];
  const summary = employeeData?.requestSummary;
  const hours = employeeData?.hoursSummary;
  const pendingRequests = employeeData?.pendingRequests ?? [];

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">Dashboard</h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Your schedule and requests</p>
          </div>
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

        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">
          <StatCard label="Shifts This Week" value={upcomingShifts.length + completedShifts.length} />
          <StatCard
            label="Hours Worked This Week"
            value={hours ? `${hours.workedHours.toFixed(1)}h` : "—"}
          />
          <StatCard
            label="Hours Remaining This Week"
            value={hours ? `${hours.remainingHours.toFixed(1)}h` : "—"}
            color="text-accent"
          />
        </div>

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-8">
          <WeeklyHoursChart upcomingShifts={upcomingShifts} completedShifts={completedShifts} />
          <RequestStatusChart summary={summary} />
        </div>

        {/* Pending swap requests requiring your action */}
        {pendingRequests.length > 0 && (
          <div className="mb-8">
            <h2 className="text-[13px] font-semibold text-ink-700 uppercase tracking-wider mb-3">
              Swap Requests Pending Your Approval
            </h2>
            <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft overflow-hidden">
              <table className="w-full text-[13px]">
                <thead className="bg-ink-50 text-[11px] uppercase text-ink-500">
                  <tr>
                    <th className="px-4 py-2 text-left">Requester</th>
                    <th className="px-4 py-2 text-left">Date</th>
                    <th className="px-4 py-2 text-left">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-ink-100">
                  {pendingRequests.map((r) => (
                    <tr key={r.approvalId} className="hover:bg-ink-50/40 transition">
                      <td className="px-4 py-3 text-ink-800">
                        {r.requesterFirstName} {r.requesterLastName}
                      </td>
                      <td className="px-4 py-3 text-[12px] text-ink-500 font-mono">
                        {formatDate(r.createdAt)}
                      </td>
                      <td className="px-4 py-3">
                        <div className="flex gap-1.5">
                          <button
                            disabled={actionLoading === r.approvalId}
                            onClick={() => handleTargetDecision(r, true)}
                            className="px-2.5 py-1 text-[11px] font-medium rounded-md bg-mint-soft text-mint-ink hover:bg-mint-ink hover:text-white transition disabled:opacity-50"
                          >
                            Accept
                          </button>
                          <button
                            disabled={actionLoading === r.approvalId}
                            onClick={() => handleTargetDecision(r, false)}
                            className="px-2.5 py-1 text-[11px] font-medium rounded-md bg-rose-soft text-rose-ink hover:bg-rose-ink hover:text-white transition disabled:opacity-50"
                          >
                            Decline
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Upcoming shifts */}
        <h2 className="text-[13px] font-semibold text-ink-700 uppercase tracking-wider mb-3">
          Upcoming Shifts
        </h2>
        {isLoading ? (
          <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft px-4 py-6 text-center text-[13px] text-ink-400 mb-6">
            Loading...
          </div>
        ) : (
          <div className="mb-6">
            <ShiftTable shifts={upcomingShifts} emptyLabel="No upcoming shifts this week" />
          </div>
        )}

        {/* Completed shifts */}
        {!isLoading && (
          <>
            <h2 className="text-[13px] font-semibold text-ink-700 uppercase tracking-wider mb-3">
              Completed Shifts
            </h2>
            <ShiftTable shifts={completedShifts} emptyLabel="No completed shifts this week" />
          </>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default EmployeeDashboard;
