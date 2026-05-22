import { useEffect } from "react";
import Footer from "../components/shared/Footer";
import useUserStore from "../../store/userStore";
import ShiftAssignmentBody from "./ShiftAssignmentBody";
import { useState } from "react";


const ShiftAssignments = () => {
  const { users, fetchUsers } = useUserStore(state => state);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const employees = users.filter(user => user.role === "USER");

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);


  return (
    <div className="flex flex-col flex-1">
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Shift assignments
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Employees</p>
          </div>
        </div>
        <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
          <table className="w-full text-[13px]">
            <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
              <tr>
                <th className="text-left font-medium px-4 py-2.5">Name</th>
                <th className="text-left font-medium px-4 py-2.5"> Status</th>
                <th className="text-left font-medium px-4 py-2.5">Hours this week</th>
                <th className="text-left font-medium px-4 py-2.5">Contact</th>
                <th className="text-center font-medium px-4 py-2.5">Assign employee</th>
              </tr>
            </thead>
            {employees.map(user => <ShiftAssignmentBody key={user.id} user={user} selectedUsers={selectedUsers} setSelectedUsers={setSelectedUsers}></ShiftAssignmentBody>)}
          </table>
        </div>

      </main>
      <Footer />
    </div>
  );
};

export default ShiftAssignments;
