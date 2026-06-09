import { useEffect, useState } from "react";
import { Link } from "react-router";
import Footer from "../components/shared/Footer";
import useUserStore from "../../store/userStore";
import useAuthStore from "../../store/authStore";
import { FiEdit2, FiTrash2, FiRotateCcw } from "react-icons/fi";
import { formatDateTimeForFrontend } from "./../../utils/formatDateTime";
import UserFilter from "./UserFilter";
import Pagination from "../components/shared/Pagination";
import ConfirmationModal from "../components/shared/ConfirmationModal";

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

  ILL: {
    bg: "bg-yellow-100",
    text: "text-yellow-800",
    dot: "bg-yellow-500",
    label: "ILL",
  },

  VACATION: {
    bg: "bg-yellow-100",
    text: "text-yellow-800",
    dot: "bg-yellow-500",
    label: "On vacation",
  },

  ABSENCE: {
    bg: "bg-yellow-100",
    text: "text-yellow-800",
    dot: "bg-yellow-500",
    label: "Absence",
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
  const {
    users,
    isLoading,
    searchUsers,
    removeUser,
    makeActive,
    totalPages,
    currentPage,
  } = useUserStore();
  const role = useAuthStore((state) => state.user.role);
  const [page, setPage] = useState(0);
  const [modal, setModal] = useState(null);

  const [filters, setFilters] = useState({
    status: null,
    role: null,
    searchByFullName: null,
  });

  const handleFilterChange = (key, value) => {
    setFilters((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  useEffect(() => {
    searchUsers({ ...filters, page: page });
  }, [searchUsers, filters, page]);

  const handleRestore = (id) => {
    const user = users.find((u) => u.id === id);

    setModal({
      title: "Restore user",
      message: `Would you like to restore ${user.firstName + " " + user.lastName}?`,
      confirmButton: "Restore",
      onConfirm: async () => {
        makeActive(id);
      },
    });
  };

  const handleDelete = (id) => {
    const user = users.find((u) => u.id === id);

    setModal({
      title: "Remove user",
      message: `Would you like to remove ${user.firstName + " " + user.lastName}?`,
      confirmButton: "Remove",
      onConfirm: async () => {
        removeUser(id);
      },
    });
  };

  return (
    <div className="flex flex-col flex-1">
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
        <div>
          <UserFilter filters={filters} onFilterChange={handleFilterChange} />
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

                    <th className="font-medium px-4 py-2.5 w-26">Out From</th>
                    <th className="text-left font-medium px-4 py-2.5 w-26">
                      Out Until
                    </th>

                    <th className="text-center font-medium px-4 py-2.5 w-60">
                      Contact
                    </th>
                    {role === "ADMIN" && (
                      <th className="text-right font-medium px-4 py-2.5">
                        Actions
                      </th>
                    )}
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

                      {/* OUT FROM TILL */}
                      <td className="px-4 py-3 font-mono text-[12px] text-ink-700 text-center">
                        {formatDateTimeForFrontend(user.outFrom) ?? ""}
                      </td>

                      <td className="px-4 py-3 font-mono text-[12px] text-ink-700 text-center">
                        {formatDateTimeForFrontend(user.outTill) ?? ""}
                      </td>

                      {/* CONTACT */}
                      <td className="px-4 py-3 text-ink-500 text-[12px]">
                        {user.email}
                      </td>

                      {/* ACTIONS */}
                      {role === "ADMIN" && (
                        <td className="px-4 py-3 text-right">
                          <div className="inline-flex gap-1.5">
                            <Link to={`/${role}/users/${user.id}/edit`}>
                              <button className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-white border border-ink-200 hover:bg-ink-50 px-2.5 py-1 text-ink-700 ">
                                <FiEdit2 size={13} />
                              </button>
                            </Link>

                            <button
                              onClick={() => handleDelete(user.id)}
                              className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-rose-soft hover:bg-rose-soft/80 border border-rose-ink/20 px-2.5 py-1 text-rose-ink "
                            >
                              <FiTrash2 size={13} />
                            </button>

                            <button
                              onClick={() => handleRestore(user.id)}
                              className="w-8 h-8 flex items-center justify-center rounded-md transition-colors bg-mint-soft hover:bg-mint-soft/80 border border-mint-ink/20 px-2.5 py-1 text-mint-ink "
                            >
                              <FiRotateCcw size={13} />
                            </button>
                          </div>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
        <Pagination
          totalPages={totalPages}
          currentPage={currentPage}
          setPage={setPage}
        />
      </main>
      <ConfirmationModal modal={modal} setModal={setModal} />
      <Footer />
    </div>
  );
};

export default UsersPage;
