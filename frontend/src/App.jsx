import "./index.css";
import { Routes, Route, Navigate } from "react-router";
import AuthProvider from "./pages/auth/AuthProvider";
import AdminPanel from "./pages/AdminPanel";
import ManagerPanel from "./pages/ManagerPanel";
import Login from "./pages/auth/login/Login";
import ShiftsPage from "./pages/shifts/ShiftsPage";
import ShiftCreatePage from "./pages/shifts/ShiftCreatePage";
import ShiftEditPage from "./pages/shifts/ShiftEditPage";
import UsersPage from "./pages/user/UsersPage";
import UserCreatePage from "./pages/user/UserCreatePage";
import UserEditPage from "./pages/user/UserEditPage";
import UserPanel from "./pages/UserPanel";
import PrivateRoute from "./pages/components/shared/PrivateRoute";
import Schedule from "./pages/schedule/Schedule";
import MySchedule from "./pages/mySchedule/MySchedule";
import Dashboard from "./pages/dashboard/Dashboard";
import Requests from "./pages/Requests/Requests";
import Employees from "./pages/employees/Employees";

function App() {
  return (
    <AuthProvider>
      <Routes>

        <Route element={<PrivateRoute publicPage />}>
          <Route path="login" element={<Login />} />
        </Route>

        <Route element={<PrivateRoute userOnly />}>
          <Route path="user" element={<UserPanel />}>
            <Route index element={<Dashboard />} />
            <Route path="schedule" element={<Schedule />} />
            <Route path="myschedule" element={<MySchedule />} />
            <Route path="requests" element={<Requests />} />
            <Route path="users" element={<UsersPage />} />
          </Route>
        </Route>

        <Route element={<PrivateRoute managerOnly />}>
          <Route path="manager" element={<ManagerPanel />} >
            <Route index element={<Dashboard />} />
            <Route path="schedule" element={<Schedule />} />
            <Route path="shifts" element={<ShiftsPage />} />
            <Route path="shifts/new" element={<ShiftCreatePage />} />
            <Route path="shifts/:id/edit" element={<ShiftEditPage />} />
            {/* <Route path="employees" element={<Employees />} /> */}
            <Route path="users" element={<UsersPage />} />
            <Route path="users/new" element={<UserCreatePage />} />
            <Route path="users/:id/edit" element={<UserEditPage />} />

          </Route>
        </Route>

        <Route element={<PrivateRoute adminOnly />}>
          <Route path="admin" element={<AdminPanel />} >
            <Route index element={<Dashboard />} />
            <Route path="schedule" element={<Schedule />} />
            <Route path="myschedule" element={<MySchedule />} />
            <Route path="shifts" element={<ShiftsPage />} />
            <Route path="shifts/new" element={<ShiftCreatePage />} />
            <Route path="shifts/:id/edit" element={<ShiftEditPage />} />
            <Route path="requests" element={<Requests />} />
            {/* <Route path="employees" element={<Employees />} /> */}
            <Route path="users" element={<UsersPage />} />
            <Route path="users/new" element={<UserCreatePage />} />


          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/login" />} />

      </Routes>
    </AuthProvider>
  );
}

export default App;
