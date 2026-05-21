import Sidebar from "./components/profile/Sidebar";
import ProfileHeader from "./components/profile/ProfileHeader";
import useAuthStore from "../store/authStore";
import { Outlet } from "react-router";

const AdminPanel = () => {

  const role = useAuthStore(state => state.user.role); 

  return (
    <main className="flex w-full">
      <Sidebar role={role} />
      <div className="flex-1">
        <ProfileHeader />
        <Outlet></Outlet>
      </div>
    </main>
  )
}

export default AdminPanel
