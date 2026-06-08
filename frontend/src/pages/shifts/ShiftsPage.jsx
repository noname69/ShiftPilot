import { useEffect, useState } from "react";
import { Link } from "react-router";
import toast from "react-hot-toast";
import Footer from "../components/shared/Footer";
import useShiftStore from "../../store/shiftStore";
import useAuthStore from "../../store/authStore";
import { FiEdit2, FiTrash2 } from "react-icons/fi";
import { HiEye } from "react-icons/hi";
import { formatDate, formatTime } from "../../utils/formatDateTime";
import ConfirmationModal from "../components/shared/ConfirmationModal";
import ShiftFilter from "../components/shared/ShiftFilter";
import { FiChevronLeft, FiChevronRight } from "react-icons/fi";
import { FiUserCheck } from "react-icons/fi";

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
    bg: "bg-slate-100",
    text: "text-slate-700",
    dot: "bg-slate-500",
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
  const [filters, setFilters] = useState({
    status: null,
    dateFrom: null,
    dateTo: null,
    createdBy: "",
  });

  const {
    shifts,
    isLoading,
    fetchShifts,
    removeShift,
    totalPages,
    currentPage,
  } = useShiftStore();
  const [page, setPage] = useState(0);

  const { user } = useAuthStore((state) => state);
  const role = user?.role.toLowerCase();
  const [modal, setModal] = useState(null);

  useEffect(() => {
    fetchShifts({ ...filters, page: page, size: 10 });
  }, [fetchShifts, filters, page]);

  const handleCancel = (id, title) => {
    setModal({
      title: "Cancel shift",
      message: `Cancel shift "${title}"? All employee assignments will be removed.`,
      rejectButton: "Cancel shift",
      onReject: async () => {
        try {
          await removeShift(id);
          toast.success("Shift cancelled successfully");
        } catch (error) {
          toast.error(
            error?.response?.data?.message ?? "Failed to cancel shift",
          );
        }
      },
    });
  };

  const handleFilterChange = (key, value) => {
    setFilters((prev) => ({
      ...prev,
      [key]: value,
    }));
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
          <div className="flex gap-4">
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
            {role !== "user" && (
              <Link to={`/${role}/shift-drafts`}>
                <button className="inline-flex items-center gap-1.5 text-[13px] font-medium text-white bg-ink-900 hover:bg-ink-800 px-3 py-1.5 rounded-md shadow-soft transition-colors">
                  Shift drafts
                </button>
              </Link>
            )}
          </div>
        </div>
        <div>
          <ShiftFilter filters={filters} onFilterChange={handleFilterChange} />
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
                        <div className="flex justify-end gap-2">
                          {role !== "user" && shift.status !== "CANCELLED" ? (
                            <>
                              <Link to={`/${role}/shifts/${shift.id}/edit`}>
                                <button className="inline-flex items-center gap-1 text-[12px] font-medium bg-white border border-ink-200 hover:bg-ink-50 px-2.5 py-1 rounded-md text-ink-700 transition-colors">
                                  <FiEdit2 size={13} />
                                </button>
                              </Link>

                              <button
                                onClick={() =>
                                  handleCancel(shift.id, shift.title)
                                }
                                className="inline-flex items-center gap-1 text-[12px] font-medium bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 rounded-md text-rose-ink transition-colors"
                              >
                                <FiTrash2 size={13} />
                              </button>

                              <Link
                                to={`/${role}/shifts/${shift.id}/assign-shift`}
                              >
                                <button className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-mint-soft border border-mint-ink/20 text-mint-ink hover:bg-mint-soft/80">
                                  <FiUserCheck size={13} />
                                </button>
                              </Link>
                            </>
                          ) : null}
                          <Link
                            to={`/${role}/shifts/${shift.id}/shift-requests`}
                          >
                            <button className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-blue-50 border-blue-300 text-blue-ink hover:bg-blue-soft/80">
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
      </main>
      <Footer />
      <ConfirmationModal modal={modal} setModal={setModal} />
    </div>
  );
};

export default ShiftsPage;
