import { useEffect, useState } from "react";
import useDashboardStore from "../../store/dashboardStore";
import Footer from "../components/shared/Footer";
import { formatDate } from "../../utils/formatDateTime";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import { processManagerRequest } from "../../api/dashboard";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer,
  PieChart, Pie, Cell,
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

const ATTENDANCE_CONFIG = {
  ON_SHIFT: { bg: "bg-mint-soft", text: "text-mint-ink", dot: "bg-mint-ink", label: "On Shift" },
  ON_LEAVE: { bg: "bg-amber-soft", text: "text-amber-ink", dot: "bg-amber-ink", label: "On Leave" },
  UNSCHEDULED: { bg: "bg-ink-100", text: "text-ink-600", dot: "bg-ink-400", label: "Unscheduled" },
};

const STATUS_LABEL = {
  PENDING_TARGET_APPROVAL: "Awaiting target",
  PENDING_MANAGER_APPROVAL: "Awaiting you",
};

const TYPE_LABEL = {
  SWAP: "Swap",
  VACATION: "Vacation",
  ILL: "Illness",
  ABSENCE: "Absence",
};

const AttendanceDonut = ({ todayAttendance }) => {
  const counts = todayAttendance.reduce((acc, e) => {
    acc[e.status] = (acc[e.status] || 0) + 1;
    return acc;
  }, {});
  const data = [
    { name: "On Shift", value: counts.ON_SHIFT || 0, color: "#1F6B3A" },
    { name: "On Leave", value: counts.ON_LEAVE || 0, color: "#7A5712" },
    { name: "Unscheduled", value: counts.UNSCHEDULED || 0, color: "#9C9C95" },
  ].filter((d) => d.value > 0);
  const total = todayAttendance.length;
  if (!total) return null;
  return (
    <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
      <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-4">Attendance Overview</p>
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

const RequestComparisonChart = ({ swapSummary, leaveSummary }) => {
  const data = [
    { name: "Pending", swap: swapSummary.pendingCount, leave: leaveSummary.pendingCount },
    { name: "Approved", swap: swapSummary.approvedCount, leave: leaveSummary.approvedCount },
    { name: "Rejected", swap: swapSummary.rejectedCount, leave: leaveSummary.rejectedCount },
  ];
  return (
    <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
      <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-4">Requests by Status</p>
      <ResponsiveContainer width="100%" height={130}>
        <BarChart data={data} barCategoryGap="35%" barGap={3}>
          <XAxis dataKey="name" tick={{ fontSize: 11, fill: "#6B6B66" }} axisLine={false} tickLine={false} />
          <YAxis hide allowDecimals={false} />
          <Tooltip
            cursor={{ fill: "#F4F4F2" }}
            content={({ active, payload, label }) =>
              active && payload?.length ? (
                <div className="bg-white border border-ink-200 rounded-lg px-2.5 py-1.5 text-[12px] shadow-soft space-y-0.5">
                  <p className="text-ink-500 font-medium mb-1">{label}</p>
                  {payload.map((p) => (
                    <p key={p.name} className="text-ink-800">
                      <span className="font-semibold">{p.value}</span> {p.name}
                    </p>
                  ))}
                </div>
              ) : null
            }
          />
          <Legend iconType="square" iconSize={8} wrapperStyle={{ fontSize: 11, color: "#6B6B66" }} />
          <Bar dataKey="swap" name="Swap" fill="#0F0F10" radius={[3, 3, 0, 0]} />
          <Bar dataKey="leave" name="Leave" fill="#3B5BDB" radius={[3, 3, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};


const CoverageCard = ({ coverage }) => {
  const { assignedEmployees, minEmployees, understaffedShiftsCount } = coverage;
  const pct = minEmployees > 0 ? Math.min((assignedEmployees / minEmployees) * 100, 100) : 100;
  const met = assignedEmployees >= minEmployees;
  const gap = Math.max(minEmployees - assignedEmployees, 0);

  return (
    <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
      <div className="flex items-center justify-between mb-4">
        <p className="text-[11px] uppercase tracking-wider text-ink-500">Week Coverage</p>
        {understaffedShiftsCount > 0 && (
          <span className="text-[11px] font-medium text-rose-ink bg-rose-soft px-2 py-0.5 rounded-full">
            {understaffedShiftsCount} understaffed {understaffedShiftsCount === 1 ? "shift" : "shifts"}
          </span>
        )}
      </div>

      <div className="flex items-end justify-between mb-2">
        <span className="text-[13px] text-ink-600">
          <span className="text-[28px] font-semibold text-ink-900 leading-none">{assignedEmployees}</span>
          <span className="text-ink-400 mx-1">/</span>
          {minEmployees} required
        </span>
        {gap > 0 ? (
          <span className="text-[12px] text-rose-ink font-medium">{gap} short</span>
        ) : (
          <span className="text-[12px] text-mint-ink font-medium">Fully covered</span>
        )}
      </div>

      <div className="h-2.5 w-full bg-ink-100 rounded-full overflow-hidden">
        <div
          className={`h-full rounded-full transition-all duration-500 ${met ? "bg-mint-ink" : "bg-rose-ink"}`}
          style={{ width: `${pct}%` }}
        />
      </div>
    </div>
  );
};

const ManagerDashboard = () => {
  const [weekOffset, setWeekOffset] = useState(0);
  const [actionLoading, setActionLoading] = useState(null);
  const { managerData, isLoading, fetchManagerDashboard } = useDashboardStore((s) => s);

  const { weekStart, weekEnd } = getWeekRange(weekOffset);

  useEffect(() => {
    fetchManagerDashboard(weekStart, weekEnd);
  }, [weekOffset, fetchManagerDashboard, weekStart, weekEnd]);

  const handleDecision = async (r, decision) => {
    setActionLoading(r.approvalId);
    try {
      await processManagerRequest({
        approvalId: r.approvalId,
        requestId: r.requestId,
        requestType: r.type,
        decision,
        comment: null,
      });
      fetchManagerDashboard(weekStart, weekEnd);
    } catch (e) {
      console.error(e);
    } finally {
      setActionLoading(null);
    }
  };

  if (isLoading || !managerData) {
    return (
      <div className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <p className="text-[13px] text-ink-500">Loading...</p>
      </div>
    );
  }

  const { swapSummary, leaveSummary, pendingRequests, todayAttendance, coverage } = managerData;

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">Dashboard</h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Overview for your team</p>
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

        {coverage && (
          <div className="mb-8">
            <CoverageCard coverage={coverage} />
          </div>
        )}

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-8">
          <RequestComparisonChart swapSummary={swapSummary} leaveSummary={leaveSummary} />
          <AttendanceDonut todayAttendance={todayAttendance} />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Pending requests */}
          <div>
            <h2 className="text-[13px] font-semibold text-ink-700 uppercase tracking-wider mb-3">
              Pending Action
            </h2>
            <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft overflow-hidden">
              {pendingRequests.length === 0 ? (
                <p className="text-[13px] text-ink-400 px-4 py-6 text-center">No pending requests</p>
              ) : (
                <table className="w-full text-[13px]">
                  <thead className="bg-ink-50 text-[11px] uppercase text-ink-500">
                    <tr>
                      <th className="px-4 py-2 text-left">Employee</th>
                      <th className="px-4 py-2 text-left">Type</th>
                      <th className="px-4 py-2 text-left">Status</th>
                      <th className="px-4 py-2 text-left">Date</th>
                      <th className="px-4 py-2 text-left">Action</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-ink-100">
                    {pendingRequests.map((r) => (
                      <tr key={r.approvalId} className="hover:bg-ink-50/40 transition">
                        <td className="px-4 py-3 text-ink-800">
                          {r.requesterFirstName} {r.requesterLastName}
                          {r.targetFirstName && (
                            <span className="text-ink-400"> → {r.targetFirstName} {r.targetLastName}</span>
                          )}
                        </td>
                        <td className="px-4 py-3 text-ink-600">
                          {TYPE_LABEL[r.type] ?? r.type}
                        </td>
                        <td className="px-4 py-3">
                          <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[11px] font-medium bg-amber-soft text-amber-ink">
                            <span className="w-1.5 h-1.5 rounded-full bg-amber-ink" />
                            {STATUS_LABEL[r.approvalStatus] ?? r.approvalStatus}
                          </span>
                        </td>
                        <td className="px-4 py-3 text-[12px] text-ink-500 font-mono">
                          {formatDate(r.createdAt)}
                        </td>
                        <td className="px-4 py-3">
                          {r.approvalStatus === "PENDING_MANAGER_APPROVAL" && (
                            <div className="flex gap-1.5">
                              <button
                                disabled={actionLoading === r.approvalId}
                                onClick={() => handleDecision(r, true)}
                                className="px-2.5 py-1 text-[11px] font-medium rounded-md bg-mint-soft text-mint-ink hover:bg-mint-ink hover:text-white transition disabled:opacity-50"
                              >
                                Approve
                              </button>
                              <button
                                disabled={actionLoading === r.approvalId}
                                onClick={() => handleDecision(r, false)}
                                className="px-2.5 py-1 text-[11px] font-medium rounded-md bg-rose-soft text-rose-ink hover:bg-rose-ink hover:text-white transition disabled:opacity-50"
                              >
                                Reject
                              </button>
                            </div>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </div>

          {/* Today's attendance */}
          <div>
            <h2 className="text-[13px] font-semibold text-ink-700 uppercase tracking-wider mb-3">
              Today's Attendance
            </h2>
            <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft overflow-hidden">
              {todayAttendance.length === 0 ? (
                <p className="text-[13px] text-ink-400 px-4 py-6 text-center">No employees found</p>
              ) : (
                <table className="w-full text-[13px]">
                  <thead className="bg-ink-50 text-[11px] uppercase text-ink-500">
                    <tr>
                      <th className="px-4 py-2 text-left">Employee</th>
                      <th className="px-4 py-2 text-left">Status</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-ink-100">
                    {todayAttendance.map((e) => {
                      const cfg = ATTENDANCE_CONFIG[e.status] ?? ATTENDANCE_CONFIG.UNSCHEDULED;
                      return (
                        <tr key={e.userId} className="hover:bg-ink-50/40 transition">
                          <td className="px-4 py-3 text-ink-800">
                            {e.firstName} {e.lastName}
                          </td>
                          <td className="px-4 py-3">
                            <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[11px] font-medium ${cfg.bg} ${cfg.text}`}>
                              <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
                              {cfg.label}
                            </span>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              )}
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default ManagerDashboard;
