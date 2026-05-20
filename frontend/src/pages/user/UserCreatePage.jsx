import { Link, useNavigate } from "react-router";
import Header from "../components/shared/Header";
import Footer from "../components/shared/Footer";
import UserForm from "./UserForm";
import useUserStore from "../../store/userStore";

const UserCreatePage = () => {
  const navigate = useNavigate();
  const { addUser, error } = useUserStore();

  const onSubmit = async (data) => {
    const result = await addUser(data);
    if (!result.success) {
      return;
    }

    navigate("/users");
  };

  return (
    <div className="flex flex-col flex-1">
      <Header />

      <main className="flex-1 flex justify-center px-5 py-7">
        <div className="w-full max-w-130">
          <div className="mb-6">
            <p className="text-[12px] text-ink-500 mb-1">Users</p>

            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              New User
            </h1>

            <p className="text-[13px] text-ink-500 mt-0.5">
              Fill in the details to create a user.
            </p>
          </div>

          <div className="my-card">
            <UserForm
              onSubmit={onSubmit}
              submitLabel="Create User"
              isEdit={false}
            />
          </div>
          {error && (
            <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-2 text-sm text-red-700">
              {error}
            </div>
          )}

          <div className="mt-4">
            <Link
              to="/users"
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

export default UserCreatePage;
