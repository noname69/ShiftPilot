import "./index.css";
import { Routes, Route } from "react-router";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/Login";
import Header from "./components/shared/Header";
import HomePage from "./pages/auth/HomePage";

function App() {
  return (
    <>
      <Routes>
        <Route index element={<HomePage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </>
  );
}

export default App;
