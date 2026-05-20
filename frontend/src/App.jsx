import "./index.css";
import { Routes, Route } from "react-router";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/login/Login";
import Header from "./pages/components/shared/Header";
import HomePage from "./pages/HomePage";
import UserPanel from "./pages/user/UserPanel";
import ShiftsPage from "./pages/shifts/ShiftsPage";
import ShiftCreatePage from "./pages/shifts/ShiftCreatePage";
import ShiftEditPage from "./pages/shifts/ShiftEditPage";

function App() {
  return (
    <>
      <Routes>
        <Route index element={<HomePage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/user" element={<UserPanel />} />
        <Route path="/shifts" element={<ShiftsPage />} />
        <Route path="/shifts/new" element={<ShiftCreatePage />} />
        <Route path="/shifts/:id/edit" element={<ShiftEditPage />} />
      </Routes>
    </>
  );
}

export default App;
