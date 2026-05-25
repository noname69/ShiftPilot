import { useState, useEffect } from "react";
import toast from "react-hot-toast";
import Footer from "../components/shared/Footer";
import ShiftAssignmentRequestsBody from "./ShiftAssigmentRequestsBody";
import useShiftAssignmentsStore from "../../store/shiftAssignmentsStore";
import { useParams } from "react-router";
import ConfirmationModal from "../components/shared/ConfirmationModal"

const ShiftAssignmentRequests = () => {
  const { shiftId } = useParams();
  const { getShiftAssignees, assignees, removeAssignment } = useShiftAssignmentsStore(state => state);
  const [modal, setModal] = useState(null);

  useEffect(() => {
    if (shiftId) {
      getShiftAssignees(shiftId);
    }
  }, [getShiftAssignees, shiftId]);

  const handleRemove = (userId, firstName, lastName) => {
    setModal({
      title: "Remove employee from shift",
      message: `Remove ${firstName} ${lastName} from this shift?`,
      rejectButton: "Remove",
      onReject: async () => {
        try {
          await removeAssignment(shiftId, userId);
          toast.success(`${firstName} ${lastName} removed from shift`);
        } catch (error) {
          toast.error(error?.response?.data?.message ?? "Failed to remove employee");
        }
      },
    });
  };

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Handle Shift Assignments
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Employees</p>
          </div>
        </div>
        <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
          <table className="w-full text-[13px]">
            <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
              <tr>
                <th className="text-left font-medium px-4 py-2.5">Name</th>
                <th className="text-center font-medium px-4 py-2.5"> Status</th>
                <th className="text-center font-medium px-4 py-2.5">Hours this week</th>
                <th className="text-left font-medium px-4 py-2.5">Contact</th>
                <th className="text-center font-medium px-4 py-2.5">Actions</th>
              </tr>
            </thead>
            {assignees.map(assignee =>
              <ShiftAssignmentRequestsBody
                key={assignee.id}
                assignee={assignee}
                onRemove={handleRemove}
              />)}
          </table>
        </div>
      </main>
      <ConfirmationModal modal={modal} setModal={setModal} />
      <Footer />
    </div>
  );
};

export default ShiftAssignmentRequests;
