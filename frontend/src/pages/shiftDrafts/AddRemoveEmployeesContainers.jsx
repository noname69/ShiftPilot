import useUserStore from "../../store/userStore";
import { useEffect, useState } from "react";
import { IoMdAddCircle } from "react-icons/io";
import { IoMdRemoveCircle } from "react-icons/io";

const AddRemoveEmployeesContainers = ({ addedUsers, setAddedUsers }) => {
  const { fetchUsers, users } = useUserStore((state) => state);
  const [avalaibleUsers, setAvailableUsers] = useState([]);


  const handleAddUser = (selectedUser) => {
    const updatedAvailableUsers = avalaibleUsers.filter((user) => user.id !== selectedUser.id);
    const updatedAddedUsers = [...addedUsers, selectedUser];

    setAvailableUsers(updatedAvailableUsers);
    setAddedUsers(updatedAddedUsers);
  };

  const handleRemoveUser = (selectedUser) => {
    const updatedAddedUsers = addedUsers.filter((user) => user.id !== selectedUser.id);
    const updatedAvailableUsers = [...avalaibleUsers, selectedUser];

    setAddedUsers(updatedAddedUsers);
    setAvailableUsers(updatedAvailableUsers);
  };

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  useEffect(() => {
    const setUsers = () => {
      if (users.length && avalaibleUsers.length === 0) {
        setAvailableUsers(users?.filter(user => user.role === "USER"));
      }
    };

    setUsers();
  }, [users, avalaibleUsers.length]);

  return (
    <div className="flex flex-col gap-4">
      <div className="h-40 overflow-y-auto border rounded-2xl border-ink-300 p-2">
        <p className="font-bold">Available employees</p>
        {avalaibleUsers.map((user) => {
          return (
            <div
              className="flex gap-4 items-center justify-between mb-2"
              key={user.id}
            >
              <div className="flex gap-4">
                <div className="flex items-center gap-2.5 w-40 overflow-hidden">
                  <div className="w-7 h-7 rounded-full bg-violet-soft text-violet-ink text-[11px] font-semibold flex items-center justify-center">
                    {user.firstName?.[0]}
                    {user.lastName?.[0]}
                  </div>

                  <div className="font-medium text-ink-900">
                    {user.firstName} {user.lastName}
                  </div>
                </div>
                <div>{user.email}</div>
              </div>

              <button
                className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-mint-soft border border-mint-ink/20 text-mint-ink hover:bg-mint-soft/80"
                onClick={() => handleAddUser(user)}
              >
                <IoMdAddCircle size={20} />
              </button>
            </div>
          );
        })}
      </div>
      <div className="h-40 overflow-y-auto border rounded-2xl border-ink-300 p-2">
        <p className="font-bold">Added employees</p>
        {addedUsers?.map((user) => {
          return (
            <div
              className="flex gap-4 items-center justify-between mb-2"
              key={user.id}
            >
              <div className="flex gap-4">
                <div className="flex items-center gap-2.5 w-40 overflow-hidden">
                  <div className="w-7 h-7 rounded-full bg-violet-soft text-violet-ink text-[11px] font-semibold flex items-center justify-center">
                    {user.firstName?.[0]}
                    {user.lastName?.[0]}
                  </div>

                  <div className="font-medium text-ink-900">
                    {user.firstName} {user.lastName}
                  </div>
                </div>
                <div>{user.email}</div>
              </div>

              <button
                className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-red-100 border border-red-600 text-red-600 hover:bg-red-200"
                onClick={() => handleRemoveUser(user)}
              >
                <IoMdRemoveCircle size={20} />
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default AddRemoveEmployeesContainers;
