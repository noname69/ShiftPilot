import useShiftStore from "../../store/shiftStore";
import { useEffect, useState } from "react";
import Footer from "../components/shared/Footer";
import MyScheduleBody from "./MyScheduleBody";
import LeaveRequestModal from "./LeaveRequestModal";
import { useParams } from "react-router";
import useLeaveStore from "../../store/leaveStore";
import MyScheduleFilter from "./MyScheduleFilter";
import Pagination from "../components/shared/Pagination";

const MySchedule = () => {
  const { userShifts, fetchUserShifts, totalPages, currentPage } =
    useShiftStore((state) => state);
  const { assigneeId } = useParams();
  const [modal, setModal] = useState(null);
  const { sendLeaveRequest } = useLeaveStore((state) => state);
  const [page, setPage] = useState(0);

  const [filters, setFilters] = useState({
    shiftStatus: null,
    shiftDate: null,
  });

  const handleFilterChange = (field, value) => {
    setFilters((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const isSwapMode = !!assigneeId;

  const handleLeaveRequest = () => {
    setModal({
      title: "Leave request",
      message: "Apply leave request",
      confirmButton: "Apply",
      onConfirm: async (formData) => {
        await sendLeaveRequest(formData);
        fetchUserShifts();
      },
    });
  };

  useEffect(() => {
    fetchUserShifts({ ...filters, page: page });
  }, [fetchUserShifts, page, filters]);

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              My Schedule
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Shifts</p>
          </div>
          <div>
            <button className="my-btn-primary" onClick={handleLeaveRequest}>
              Request Leave
            </button>
          </div>
        </div>

        <MyScheduleFilter
          filters={filters}
          onFilterChange={handleFilterChange}
        />

        <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
          <table className="w-full text-[13px]">
            <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
              <tr>
                <th className="text-left font-medium px-4 py-2.5">Title</th>
                <th className="text-left font-medium px-4 py-2.5">
                  Description
                </th>
                <th className="text-left font-medium px-4 py-2.5">Date</th>
                <th className="text-left font-medium px-4 py-2.5">Time</th>
                <th className="text-center font-medium px-4 py-2.5">
                  Min. Staff
                </th>
                <th className="text-center font-medium px-4 py-2.5">Status</th>
                <th className="text-center font-medium px-4 py-2.5">
                  Created by
                </th>
                <th className="text-center font-medium px-4 py-2.5">Actions</th>
              </tr>
            </thead>

            {userShifts.map((shift) => (
              <MyScheduleBody
                key={shift.id}
                shift={shift}
                isSwapMode={isSwapMode}
                selectedShiftId={assigneeId}
              />
            ))}
          </table>
        </div>

        <LeaveRequestModal modal={modal} setModal={setModal} />
        <Pagination
          totalPages={totalPages}
          currentPage={currentPage}
          setPage={setPage}
        />
      </main>

      <Footer />
    </div>
  );
};

export default MySchedule;
