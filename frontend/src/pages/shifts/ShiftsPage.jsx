import { useEffect } from "react";
import { Link } from "react-router";
import Footer from "../components/shared/Footer";
import useShiftStore from "../../store/shiftStore";
import useAuthStore from "../../store/authStore";
import { FiEdit2, FiTrash2 } from "react-icons/fi";
import { RiAddLargeLine } from "react-icons/ri";
import { HiEye } from "react-icons/hi";
import { formatDate, formatTime } from "../../utils/formatDateTime";


const STATUS_CONFIG = {
  OPEN: {
    bg: "bg-mint-soft",
    text: "text-mint-ink",
    dot: "bg-mint-ink",
    label: "Open",
  },
  ONGOING: {
    bg: "bg-amber-soft",
    text: "text-amber-ink",
    dot: "bg-amber-ink",
    label: "Ongoing",
  },
  COMPLETED: {
    bg: "bg-ink-100",
    text: "text-ink-600",
    dot: "bg-ink-400",
    label: "Completed",
  },
  CANCELLED: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Cancelled",
  },
};

const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? STATUS_CONFIG.OPEN;
  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] px-1.5 py-0.5 rounded font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};

const ShiftsPage = () => {

  const { shifts, isLoading, fetchShifts, removeShift } = useShiftStore();

  const { user } = useAuthStore(state => state);
  const role = user?.role.toLowerCase();

  useEffect(() => {
    fetchShifts();
  }, [fetchShifts]);

  const handleDelete = (id, title) => {
    if (window.confirm(`Delete shift "${title}"?`)) {
      removeShift(id);
    }
  };

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Shifts
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">
              Manage all scheduled shifts
            </p>
          </div>
          <Link to={`/${role}/shifts/new`}>
            <button className="inline-flex items-center gap-1.5 text-[13px] font-medium text-white bg-ink-900 hover:bg-ink-800 px-3 py-1.5 rounded-md shadow-soft transition-colors">
              <svg
                width="14"
                height="14"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2.2"
              >
                <path d="M12 5v14M5 12h14" />
              </svg>
              New shift
            </button>
          </Link>
        </div>

        {isLoading ? (
          <div className="flex items-center justify-center py-20 text-[13px] text-ink-400">
            Loading shifts…
          </div>
        ) : (
          <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
            {shifts.length === 0 ? (
              <div className="flex flex-col items-center justify-center py-20 gap-3">
                <p className="text-[14px] text-ink-500">No shifts yet.</p>
                <Link
                  to={`/${role}/shifts/new`}
                  className="inline-flex items-center gap-1.5 text-[13px] font-medium text-white bg-ink-900 hover:bg-ink-800 px-3 py-1.5 rounded-md shadow-soft transition-colors cursor-pointer"
                >
                  <svg
                    width="13"
                    height="13"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2.2"
                  >
                    <path d="M12 5v14M5 12h14" />
                  </svg>
                  Create your first shift
                </Link>
              </div>
            ) : (
              <table className="w-full text-[13px]">
                <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
                  <tr>
                    <th className="text-left font-medium px-4 py-2.5">Title</th>
                    <th className="text-left font-medium px-4 py-2.5 hidden md:table-cell">
                      Description
                    </th>
                    <th className="text-left font-medium px-4 py-2.5">Date</th>
                    <th className="text-left font-medium px-4 py-2.5 hidden sm:table-cell">
                      Time
                    </th>
                    <th className="text-left font-medium px-4 py-2.5 hidden lg:table-cell">
                      Min. Staff
                    </th>
                    <th className="text-center font-medium px-4 py-2.5">
                      Status
                    </th>
                    <th className="text-left font-medium px-4 py-2.5 hidden xl:table-cell">
                      Created by
                    </th>
                    <th className="text-center font-medium px-4 py-2.5">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-ink-100">
                  {shifts.map((shift) => (
                    <tr
                      key={shift.id}
                      className="hover:bg-ink-50/60 transition-colors"
                    >
                      <td className="px-4 py-3 font-medium text-ink-900">
                        {shift.title}
                      </td>
                      <td className="px-4 py-3 text-ink-600 max-w-50 truncate hidden md:table-cell">
                        {shift.description}
                      </td>
                      <td className="px-4 py-3 text-ink-700 font-mono text-[12px]">
                        {formatDate(shift.shiftDate)}
                      </td>
                      <td className="px-4 py-3 text-ink-600 font-mono text-[12px] hidden sm:table-cell">
                        {formatTime(shift.startTime)} –{" "}
                        {formatTime(shift.endTime)}
                      </td>
                      <td className="px-4 py-3 text-ink-700 hidden lg:table-cell">
                        {shift.minEmployees}
                      </td>
                      <td className="px-4 py-3">
                        <StatusBadge status={shift.status} />
                      </td>
                      <td className="px-4 py-3 text-ink-500 hidden xl:table-cell">
                        {shift.createdByUsername}
                      </td>
                      <td className="px-4 py-3 text-right">
                        <div className="inline-flex gap-1.5">
                          {role !== "user" ? (
                            <>
                              <Link to={`/${role}/shifts/${shift.id}/edit`}>
                                <button className="inline-flex items-center gap-1 text-[12px] font-medium bg-white border border-ink-200 hover:bg-ink-50 px-2.5 py-1 rounded-md text-ink-700 transition-colors">
                                  <FiEdit2 size={13} />
                                </button>
                              </Link>

                              <button
                                onClick={() => handleDelete(shift.id, shift.title)}
                                className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 rounded-md text-rose-ink transition-colors"
                              >
                                <FiTrash2 size={13} />
                              </button>

                              <Link to={`/${role}/shifts/${shift.id}/assign-shift`}>
                                <button className="inline-flex items-center gap-1 text-[12px] font-medium bg-green-50 border border-green-300 px-2.5 py-1 rounded-md text-green-800 transition-colors">
                                  <RiAddLargeLine size={13} />
                                </button>
                              </Link>
                            </>
                          ) : null}
                          <Link to={`/${role}/shifts/${shift.id}/shift-requests`}>
                            <button
                              className="inline-flex items-center gap-1 text-[12px] font-medium bg-blue-50 border border-blue-300 px-2.5 py-1 rounded-md text-blue-800 transition-colors mr-2"
                            >
                              <HiEye size={13} />
                            </button>
                          </Link>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default ShiftsPage;
