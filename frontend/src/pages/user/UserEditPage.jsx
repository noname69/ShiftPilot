import { useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router";
import Header from "../components/shared/Header";
import Footer from "../components/shared/Footer";
import UserForm from "./UserForm";
import useUserStore from "../../store/userStore";
import useAuthStore from "../../store/authStore";

const UserEditPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const role = useAuthStore(state => state.user.role);

  const { users, fetchUsers, editUser, error } = useUserStore();

  useEffect(() => {
    if (!users.length) {
      fetchUsers();
    }
  }, [users.length, fetchUsers]);

  const user = users.find((u) => String(u.id) === id);

  const onSubmit = async (data) => {
    const result = await editUser(id, data);
    if (!result.success) {
      return;
    }
    navigate(`/${role}/users`);
  };

  return (
    <div className="flex flex-col flex-1">
      <Header />

      <main className="flex-1 flex justify-center px-5 py-7">
        <div className="w-full max-w-130">
          <div className="mb-6">
            <p className="text-[12px] text-ink-500 mb-1">Users</p>

            <h1 className="font-serif text-[32px]">Edit User</h1>
          </div>

          <div className="my-card">
            {user ? (
              <UserForm
                onSubmit={onSubmit}
                defaultValues={user}
                submitLabel="Update User"
                isEdit={true}
              />
            ) : (
              <p className="text-sm text-gray-500">Loading user...</p>
            )}
          </div>
          {error && (
            <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-2 text-sm text-red-700">
              {error}
            </div>
          )}
          <div className="mt-4">
            <Link
              to={`/${role}/users`}
              className="text-[13px] text-ink-500 hover:text-ink-900 transition-colors"
            >
              ← Back to users
            </Link>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default UserEditPage;
