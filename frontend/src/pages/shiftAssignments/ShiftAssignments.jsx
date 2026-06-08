import { useState, useEffect } from "react";
import Footer from "../components/shared/Footer";
import useUserStore from "../../store/userStore";
import ShiftAssignmentsBody from "./ShiftAssignmentsBody";
import useShiftAssignmentsStore from "../../store/shiftAssignmentsStore";
import { useParams, useNavigate, Link } from "react-router";
import useAuthStore from "../../store/authStore";
import ConfirmationModal from "../components/shared/ConfirmationModal"
import { getShiftById } from "../../api/shift";

const ShiftAssignments = () => {
  const { users, fetchUsers } = useUserStore(state => state);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const employees = users.filter(user => user.role === "USER" && user.status === "ACTIVE");
  const { shiftId } = useParams();
  const role = useAuthStore((state) => state.user.role);
  const { getShiftAssignees, assignees, assignEmployees, removeEmployeeFromShiftAssignment, overlappingUserIds } = useShiftAssignmentsStore(state => state);
  const [modal, setModal] = useState(null);
  const [shift, setShift] = useState(null);
  const navigate = useNavigate();

  const handleAddToShift = () => {
    setModal({
      title: "Add employees to shift",
      message: "Are you sure you want to add selected employees to shift?",
      confirmButton: "Add",
      onConfirm: () => {
        assignEmployees({ userIds: selectedUsers }, shiftId, navigate);
      }
    })
  }

  useEffect(() => {
    if (shiftId) {
      fetchUsers();
      getShiftAssignees(shiftId);
      getShiftById(shiftId).then(setShift).catch(console.error);
    }
  }, [fetchUsers, getShiftAssignees, shiftId]);

  const isCancelled = shift?.status === "CANCELLED";

  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Add Employees to Shift
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Employees</p>
          </div>
        </div>
        {isCancelled ? (
          <div className="bg-rose-soft border border-rose-ink/20 rounded-xl2 px-6 py-10 text-center">
            <p className="text-[14px] font-medium text-rose-ink">This shift has been cancelled.</p>
            <p className="text-[13px] text-rose-ink/70 mt-1">Employee assignments are not available for cancelled shifts.</p>
          </div>
        ) : (
          <>
            <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
              <table className="w-full text-[13px]">
                <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
                  <tr>
                    <th className="text-left font-medium px-4 py-2.5">Name</th>
                    <th className="text-center font-medium px-4 py-2.5"> Status</th>
                    <th className="text-center font-medium px-4 py-2.5">Hours this week</th>
                    <th className="text-left font-medium px-4 py-2.5">Contact</th>
                    <th className="text-center font-medium px-4 py-2.5">Assign employees</th>
                    <th className="text-center font-medium px-4 py-2.5">Actions</th>
                  </tr>
                </thead>
                {employees.map(user =>
                  <ShiftAssignmentsBody
                    key={user.id}
                    user={user}
                    selectedUsers={selectedUsers}
                    setSelectedUsers={setSelectedUsers}
                    assignees={assignees}
                    removeEmployee={removeEmployeeFromShiftAssignment}
                    shiftId={shiftId}
                    isAlreadyInAnotherShift={overlappingUserIds.includes(user.id)}
                    shiftStatus={shift?.status}
                  />)}
              </table>
            </div>
            {selectedUsers.length > 0 ? (
              <div className="flex justify-end m-5">
                <button
                  className="my-btn-primary"
                  onClick={handleAddToShift}
                >Add to shift
                </button>
              </div>
            ) : ""}
          </>
        )}
        <div className="mt-4">
          <Link
            to={`/${role?.toLowerCase()}/shifts`}
            className="text-[13px] text-ink-500 hover:text-ink-900 transition-colors"
          >
            ← Back to shifts
          </Link>
        </div>
      </main>
      <ConfirmationModal modal={modal} setModal={setModal} />
      <Footer />
    </div>
  );
};

export default ShiftAssignments;
