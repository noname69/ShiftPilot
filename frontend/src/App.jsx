import "./index.css";
import { Routes, Route } from "react-router";
import AuthProvider from "./pages/auth/AuthProvider";
import AdminPanel from "./pages/admin/AdminPanel";
import ManagerPanel from "./pages/manager/ManagerPanel";
import Login from "./pages/auth/login/Login";
import UserPanel from "./pages/user/UserPanel";
import PrivateRoute from "./components/shared/PrivateRoute";

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route path="/" element={<PrivateRoute userOnly />}>
          <Route path="/user" element={<UserPanel />} />
        </Route>

        <Route path="/" element={<PrivateRoute managerOnly />}>
          <Route path="/manager" element={<ManagerPanel />} />
        </Route>

        <Route path="/" element={<PrivateRoute adminOnly />}>
          <Route path="/admin" element={<AdminPanel />} />
        </Route>

      </Routes>
    </AuthProvider>
  );
}

export default App;
