import { useEffect } from "react";
import { Link } from "react-router";
import Header from "../components/shared/Header";
import Footer from "../components/shared/Footer";
import useUserStore from "../../store/userStore";
import useAuthStore from "../../store/authStore";

const STATUS_CONFIG = {
  ACTIVE: {
    bg: "bg-mint-soft",
    text: "text-mint-ink",
    dot: "bg-mint-ink",
    label: "Active",
  },

  INACTIVE: {
    bg: "bg-rose-soft",
    text: "text-rose-ink",
    dot: "bg-rose-ink",
    label: "Inactive",
  },
};

const StatusBadge = ({ status }) => {
  const cfg = STATUS_CONFIG[status] ?? STATUS_CONFIG.ACTIVE;
  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] px-1.5 py-0.5 rounded font-medium ${cfg.bg} ${cfg.text}`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
      {cfg.label}
    </span>
  );
};



const UsersPage = () => {

  const { users, isLoading, fetchUsers, removeUser, udeleteUser } = useUserStore();
  const role = useAuthStore(state => state.user.role);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm("Delete this user?");

    if (!confirmDelete) return;

    const success = await removeUser(id);

    if (!success) {
      console.log("Delete failed");
    }
  };

  const handleRestore = async (id) => {
    const confirmRestore = window.confirm("Restore this user?");

    if (!confirmRestore) return;
    const success = await udeleteUser(id);

    if (!success) {
      console.log("Restore failed");
    }
  };

  return (
    <div className="flex flex-col flex-1">
      <Header />
      <main className="flex-1 px-5 lg:px-8 py-7 max-w-350 mx-auto w-full">
        <div className="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <h1 className="font-serif text-[32px] leading-tight text-ink-900 tracking-tight">
              Users
            </h1>
            <p className="text-[13px] text-ink-500 mt-0.5">Manage all users</p>
          </div>
          <Link to={`/${role}/users/new`}>
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
              New user
            </button>
          </Link>
        </div>
        {isLoading ? (
          <div className="flex items-center justify-center py-20 text-[13px] text-ink-400">
            Loading users…
          </div>
        ) : (
          <div className="bg-white rounded-xl2 border border-ink-200 shadow-soft overflow-hidden">
            {users.length === 0 ? (
              <div className="flex flex-col items-center justify-center py-20 gap-3">
                <p className="text-[14px] text-ink-500">No users yet.</p>

                <Link
                  to={`/${role}/users/new`}
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
                  Create first user
                </Link>
              </div>
            ) : (
              <table className="w-full text-[13px]">
                <thead className="bg-ink-50 text-ink-500 text-[11.5px] uppercase tracking-wider border-b border-ink-200">
                  <tr>
                    <th className="text-left font-medium px-4 py-2.5">Name</th>

                    <th className="text-left font-medium px-4 py-2.5">Role</th>

                    <th className="text-left font-medium px-4 py-2.5">
                      Status
                    </th>

                    <th className="text-left font-medium px-4 py-2.5">
                      Hours this week
                    </th>

                    <th className="text-left font-medium px-4 py-2.5">
                      Contact
                    </th>

                    <th className="text-right font-medium px-4 py-2.5">
                      Actions
                    </th>
                  </tr>
                </thead>

                <tbody className="divide-y divide-ink-100">
                  {users.map((user) => (
                    <tr
                      key={user.id}
                      className="hover:bg-ink-50/60 transition-colors"
                    >
                      {/* NAME */}
                      <td className="px-4 py-3">
                        <div className="flex items-center gap-2.5">
                          <div className="w-7 h-7 rounded-full bg-violet-soft text-violet-ink text-[11px] font-semibold flex items-center justify-center">
                            {user.firstName?.[0]}
                            {user.lastName?.[0]}
                          </div>

                          <div className="font-medium text-ink-900">
                            {user.firstName} {user.lastName}
                          </div>
                        </div>
                      </td>

                      {/* ROLE */}
                      <td className="px-4 py-3 text-ink-700">{user.role}</td>

                      {/* STATUS */}
                      <td className="px-4 py-3">
                        <StatusBadge status={user.status} />
                      </td>

                      {/* HOURS */}
                      <td className="px-4 py-3 font-mono text-[12px] text-ink-700">
                        {user.weeklyHours ?? 0}h
                      </td>

                      {/* CONTACT */}
                      <td className="px-4 py-3 text-ink-500 text-[12px]">
                        {user.email}
                      </td>

                      {/* ACTIONS */}
                      <td className="px-4 py-3 text-right">
                        <div className="inline-flex gap-1.5">
                          <Link to={`/${role}/users/${user.id}/edit`}>
                            <button className="text-[12px] font-medium bg-white border border-ink-200 hover:bg-ink-50 px-2.5 py-1 rounded-md text-ink-700 transition-colors">
                              Edits
                            </button>
                          </Link>

                          <button
                            onClick={() => handleDelete(user.id)}
                            className="text-[12px] font-medium bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 rounded-md text-rose-ink transition-colors"
                          >
                            Delete
                          </button>

                          <button
                            onClick={() => handleRestore(user.id)}
                            className="text-[12px] font-medium bg-mint-soft hover:bg-mint-soft/80 border border-mint-ink/20 px-2.5 py-1 rounded-md text-mint-ink transition-colors"
                          >
                            Restore
                          </button>
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

export default UsersPage;
