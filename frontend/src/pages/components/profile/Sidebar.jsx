import { NavLink } from "react-router";
import {
  UserNavigation,
  ManagerNavigation,
  AdminNavigation,
} from "./SidebarNavigation";
import useAuthStore from "../../../store/authStore";

const Sidebar = ({ role }) => {
  const user = useAuthStore((state) => state.user);

  const roleLabels = {
    USER: "Employee",
    MANAGER: "Manager",
    ADMIN: "Admin",
  };

  const displayRole = roleLabels[user.role] ?? "";

  const initials = (
    (user.firstName?.[0] ?? "") + (user.lastName?.[0] ?? "")
  ).toUpperCase();

  const sideBarLayout =
    role === "USER"
      ? UserNavigation
      : role === "MANAGER"
        ? ManagerNavigation
        : role === "ADMIN"
          ? AdminNavigation
          : null;

  return (
    <aside className="hidden lg:flex w-61 shrink-0 flex-col min-h-screen border-r border-ink-200 bg-ink-50/60 backdrop-blur-sm">
      <div className="px-5 pt-5 pb-4 flex items-center gap-2.5">
        <div className="w-7 h-7 rounded-lg bg-ink-900 flex items-center justify-center">
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
            <path
              d="M2 7L6 11L12 3"
              stroke="white"
              strokeWidth="1.8"
              strokeLinecap="round"
              strokeLinejoin="round"
            ></path>
          </svg>
        </div>
        <div className="leading-tight">
          <div className="text-[15px] font-semibold tracking-tight text-ink-900">
            ShiftPilot
          </div>
          <div className="text-[11px] text-ink-500">Northside Café</div>
        </div>
        <button className="ml-auto text-ink-400 hover:text-ink-700">
          <svg
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <path d="M7 10l5-5 5 5M7 14l5 5 5-5"></path>
          </svg>
        </button>
      </div>

      <nav className="px-3 space-y-0.5">
        <div className="px-2 text-[11px] uppercase tracking-wider text-ink-400 mb-1.5">
          Workspace
        </div>
        <ul>
          {sideBarLayout.map((item, index) => (
            <li key={index}>
              <NavLink
                end
                to={item.href}
                state={{ name: item.name }}
                className={({ isActive }) =>
                  `tab-btn w-full flex items-center gap-2.5 px-2.5 py-1.5 rounded-md text-[13px] text-ink-600 hover:bg-ink-100 ${isActive ? "font-bold underline" : ""}`
                }
                data-target="dashboard"
                aria-current="page"
              >
                {item.icon}
                {item.name}
              </NavLink>
            </li>
          ))}
        </ul>

        <div className="px-2 text-[11px] uppercase tracking-wider text-ink-400 mb-1.5 mt-5">
          {" "}
          Account
        </div>
        <button className="w-full flex items-center gap-2.5 px-2.5 py-1.5 rounded-md text-[13px] text-ink-600 hover:bg-ink-100">
          <svg
            width="15"
            height="15"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.8"
          >
            <circle cx="12" cy="12" r="3"></circle>
            <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09a1.65 1.65 0 0 0 1.51-1 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33h0a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82v0a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
          </svg>
          Settings
        </button>
        <button className="w-full flex items-center gap-2.5 px-2.5 py-1.5 rounded-md text-[13px] text-ink-600 hover:bg-ink-100">
          <svg
            width="15"
            height="15"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.8"
          >
            <circle cx="12" cy="12" r="10"></circle>
            <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3M12 17h.01"></path>
          </svg>
          Help &amp; docs
        </button>
      </nav>

      <div className="mt-auto p-3">
        <div className="rounded-xl border border-ink-200 bg-white p-3 shadow-soft">
          <div className="flex items-center gap-2">
            <div className="w-7 h-7 rounded-full bg-linear-to-br from-ink-700 to-ink-900 text-white flex items-center justify-center text-[11px] font-semibold">
              {initials}
            </div>
            <div className="leading-tight flex-1 min-w-0">
              <div className="text-[12.5px] font-medium text-ink-800 truncate">
                {user.firstName} {user.lastName}
              </div>
              <div className="text-[11px] text-ink-500">{displayRole}</div>
            </div>
          </div>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
