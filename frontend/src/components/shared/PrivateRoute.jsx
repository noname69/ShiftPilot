import useUsersStore from "../../store/userStore";
import { Navigate, Outlet, useNavigate } from "react-router";

const PrivateRoute = ({ publicPage = false, userOnly = false, managerOnly = false, adminOnly = false }) => {

  const { user, logoutUser } = useUsersStore(state => state);
  const { username, role } = user;
  const navigate = useNavigate();
  const isUser = role === "USER";
  const isManager = role === "MANAGER";
  const isAdmin = role === "ADMIN";
  

  if (publicPage) {
    switch (true) {
      case isUser: {
        return <Navigate to="/user" />
      }
      case isManager: {
        return <Navigate to="/manager" />
      }
      case isAdmin: {
        return <Navigate to="/admin" />
      }
      default:
        return <Outlet />
    }
  }

        console.log(role)

  if (userOnly) {
    if (!isUser && username) {
      console.log(role)
      logoutUser(navigate);
      return;
    } else {
      return <Outlet />
    }
  }

  if (managerOnly) {
    if (!isManager && username) {
      logoutUser(navigate);
      return;
    } else {
      return <Outlet />
    }
  }

  if (adminOnly) {
    if (!isAdmin && username) {
      logoutUser(navigate);
      return;
    } else {
      return <Outlet />
    }
  }
}

export default PrivateRoute