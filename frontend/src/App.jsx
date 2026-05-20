import "./index.css";
import { Routes, Route } from "react-router";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/login/Login";
import HomePage from "./pages/HomePage";
import ShiftsPage from "./pages/shifts/ShiftsPage";
import ShiftCreatePage from "./pages/shifts/ShiftCreatePage";
import ShiftEditPage from "./pages/shifts/ShiftEditPage";
import UsersPage from "./pages/user/UsersPage";
import UserCreatePage from "./pages/user/UserCreatePage";
import UserEditPage from "./pages/user/UserEditPage";

function App() {
  return (
    <>
      <Routes>
        <Route index element={<HomePage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/users" element={<UsersPage />} />
        <Route path="/users/new" element={<UserCreatePage />} />
        <Route path="/users/:id/edit" element={<UserEditPage />} />
        <Route path="/shifts" element={<ShiftsPage />} />
        <Route path="/shifts/new" element={<ShiftCreatePage />} />
        <Route path="/shifts/:id/edit" element={<ShiftEditPage />} />
      </Routes>
    </>
  );
}

export default App;
