import Sidebar from "./components/profile/Sidebar"
import { Outlet } from "react-router"
import ProfileHeader from "./components/profile/ProfileHeader"
import useAuthStore from "../store/authStore"

const UserPanel = () => {

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

export default UserPanel
