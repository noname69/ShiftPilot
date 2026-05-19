import "./index.css";
import { Routes, Route } from "react-router";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/login/Login";
import Header from "./components/shared/Header";
import HomePage from "./pages/HomePage";
import UserPanel from "./pages/user/UserPanel";

function App() {
  return (
    <>
      <Routes>
        <Route index element={<HomePage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/user" element={<UserPanel />} />
      </Routes>
    </>
  );
}

export default App;
