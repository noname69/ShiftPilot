import Sidebar from "./components/profile/Sidebar";
import ProfileHeader from "./components/profile/ProfileHeader";
import { Outlet } from "react-router";

const AdminPanel = () => {
  return (
    <main className="flex w-full">
      <Sidebar />
      <div className="flex-1">
        <ProfileHeader />
        <Outlet></Outlet>
      </div>
    </main>
  )
}

export default AdminPanel
