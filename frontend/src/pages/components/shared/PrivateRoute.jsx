import useAuthStore from "../../../store/authStore";
import { Navigate, Outlet } from "react-router";

const PrivateRoute = ({ publicPage = false, userOnly = false, managerOnly = false, adminOnly = false }) => {

  const { user } = useAuthStore((state) => state);
  const { role } = user;
  const isUser = role === "USER";
  const isManager = role === "MANAGER";
  const isAdmin = role === "ADMIN";

  if (publicPage) {
    if (isAdmin) return <Navigate to="/admin" />
    if (isManager) return <Navigate to="/manager" />
    if (isUser) return <Navigate to="/user" />

    return <Outlet />
  }

  if (adminOnly) {
    if (isAdmin) return <Outlet />
    if (isManager) return <Navigate to="/manager" />
    if (isUser) return <Navigate to="/user" />
    return <Navigate to="/login"/>
  }

  if (managerOnly) {
    if (isManager) return <Outlet />
    if (isAdmin) return <Navigate to="/admin" />
    if (isUser) return <Navigate to="/user" />
    return <Navigate to="/login"/>
  }

  if (userOnly) {
    if (isUser) return <Outlet />
    if (isAdmin) return <Navigate to="/admin" />
    if (isManager) return <Navigate to="/manager" />
    return <Navigate to="/login"/>
  }
}

export default PrivateRoute