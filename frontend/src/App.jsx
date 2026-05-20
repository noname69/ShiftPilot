import "./index.css";
import { Routes, Route } from "react-router";
import AuthProvider from "./pages/auth/AuthProvider";
import AdminPanel from "./pages/admin/AdminPanel";
import ManagerPanel from "./pages/manager/ManagerPanel";
import Login from "./pages/auth/login/Login";
import UserPanel from "./pages/user/UserPanel";
import PrivateRoute from "./components/shared/PrivateRoute";
import ShiftsPage from "./pages/shifts/ShiftsPage";
import ShiftCreatePage from "./pages/shifts/ShiftCreatePage";
import ShiftEditPage from "./pages/shifts/ShiftEditPage";

function App() {
  return (
    <AuthProvider>
      <Routes>

        <Route path="/" element={<PrivateRoute publicPage />}>
          <Route path="login" element={<Login />} />
        </Route>

        <Route path="/" element={<PrivateRoute userOnly />}>
          <Route path="user" element={<UserPanel />} />
        </Route>

        <Route path="/" element={<PrivateRoute managerOnly />}>
          <Route path="manager" element={<ManagerPanel />} />
        </Route>

        <Route path="/" element={<PrivateRoute adminOnly />}>
          <Route path="admin" element={<AdminPanel />} />
        </Route>

        <Route path="/user" element={<UserPanel />} />
        <Route path="/shifts" element={<ShiftsPage />} />
        <Route path="/shifts/new" element={<ShiftCreatePage />} />
        <Route path="/shifts/:id/edit" element={<ShiftEditPage />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
