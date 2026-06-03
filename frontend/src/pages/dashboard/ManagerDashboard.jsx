import { useEffect } from "react";
import useDashboardStore from "../../store/dashboardStore";
import Footer from "../components/shared/Footer";
import { formatDate } from "../../utils/formatDateTime";

const ATTENDANCE_CONFIG = {
  ON_SHIFT: { bg: "bg-mint-soft", text: "text-mint-ink", dot: "bg-mint-ink", label: "On Shift" },
  ON_LEAVE: { bg: "bg-amber-soft", text: "text-amber-ink", dot: "bg-amber-ink", label: "On Leave" },
  UNSCHEDULED: { bg: "bg-ink-100", text: "text-ink-600", dot: "bg-ink-400", label: "Unscheduled" },
};

const STATUS_LABEL = {
  PENDING_TARGET_APPROVAL: "Target approval",
  PENDING_MANAGER_APPROVAL: "Manager approval",
};

const TYPE_LABEL = {
  SWAP: "Swap",
  VACATION: "Vacation",
  ILL: "Illness",
  ABSENCE: "Absence",
};

const StatCard = ({ label, pending, approved, rejected }) => (
  <div className="bg-white border border-ink-200 rounded-xl2 shadow-soft p-5">
    <p className="text-[11px] uppercase tracking-wider text-ink-500 mb-3">{label}</p>
    <div className="flex gap-4">
      <div>
        <p className="text-[26px] font-semibold text-ink-900 leading-none">{pending}</p>
        <p className="text-[11px] text-amber-ink mt-1">Pending</p>
      </div>
      <div className="w-px bg-ink-200" />
      <div>
        <p className="text-[26px] font-semibold text-ink-900 leading-none">{approved}</p>
        <p className="text-[11px] text-mint-ink mt-1">Approved</p>
      </div>
      <div className="w-px bg-ink-200" />
      <div>
        <p className="text-[26px] font-semibold text-ink-900 leading-none">{rejected}</p>
        <p className="text-[11px] text-rose-ink mt-1">Rejected</p>
      </div>
    </div>
  </div>
);

const ManagerDashboard = () => {
  const { managerData, isLoading, fetchManagerDashboard } = useDashboardStore((s) => s);

  useEffect(() => {
    fetchManagerDashboard();
  }, [fetchManagerDashboard]);

  if (isLoading || !managerData) {
    return (
      <div className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <p className="text-[13px] text-ink-500">Loading...</p>
      </div>
    );
  }

  const { swapSummary, leaveSummary, pendingRequests, todayAttendance } = managerData;

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="mb-6">
          <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">Dashboard</h1>
          <p className="text-[13px] text-ink-500 mt-0.5">Overview for your team</p>
        </div>

        {/* Stat cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
          <StatCard
            label="Swap Requests"
            pending={swapSummary.pendingCount}
            approved={swapSummary.approvedCount}
            rejected={swapSummary.rejectedCount}
          />
          <StatCard
            label="Leave Requests"
            pending={leaveSummary.pendingCount}
            approved={leaveSummary.approvedCount}
            rejected={leaveSummary.rejectedCount}
          />
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
