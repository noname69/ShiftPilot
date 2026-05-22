import { FaRegClock } from "react-icons/fa6";

const icons = {
  dashboard: (<svg className="nav-ic text-ink-500" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><rect x="3" y="3" width="7" height="9" rx="1.5"></rect><rect x="14" y="3" width="7" height="5" rx="1.5"></rect><rect x="14" y="12" width="7" height="9" rx="1.5"></rect><rect x="3" y="16" width="7" height="5" rx="1.5"></rect></svg>),
  schedule: (<svg className="nav-ic text-ink-500" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><rect x="3" y="4" width="18" height="17" rx="2"></rect><path d="M3 9h18M8 2v4M16 2v4"></path></svg>),
  myShedule: (<svg className="nav-ic text-ink-500" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><circle cx="12" cy="8" r="3.5"></circle><path d="M5 21c0-3.9 3.1-7 7-7s7 3.1 7 7"></path></svg>),
  shifts: (<FaRegClock className="text-ink-500" />),
  requests: (<svg className="nav-ic text-ink-500" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg>),
  employees: (<svg className="nav-ic text-ink-500" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"></path></svg>)
}

export const UserNavigation = [
  {
    name: "Dashboard",
    href: "/user",
    icon: icons.dashboard
  },
  {
    name: "Schedule",
    href: "/user/schedule",
    icon: icons.schedule
  },
  {
    name: "My Schedule",
    href: "/user/myschedule",
    icon: icons.myShedule
  },
  {
    name: "Shifts",
    href: "/user/shifts",
    icon: icons.shifts
  },
  {
    name: "Requests",
    href: "/user/requests",
    icon: icons.requests
  },
]

export const ManagerNavigation = [
  {
    name: "Dashboard",
    href: "/manager",
    icon: icons.dashboard
  },
  {
    name: "Schedule",
    href: "/manager/schedule",
    icon: icons.schedule
  },
  {
    name: "Shifts",
    href: "/manager/shifts",
    icon: icons.shifts
  },
  {
    name: "Employees",
    href: "/manager/users",
    icon: icons.employees
  },
]

export const AdminNavigation = [
  {
    name: "Dashboard",
    href: "/admin",
    icon: icons.dashboard
  },
  {
    name: "Schedule",
    href: "/admin/schedule",
    icon: icons.schedule
  },
  {
    name: "My Schedule",
    href: "/admin/myschedule",
    icon: icons.myShedule
  },
  {
    name: "Shifts",
    href: "/admin/shifts",
    icon: icons.shifts
  },
  {
    name: "Requests",
    href: "/admin/requests",
    icon: icons.requests
  },
  {
    name: "Employees",
    href: "/admin/users",
    icon: icons.employees
  },

]