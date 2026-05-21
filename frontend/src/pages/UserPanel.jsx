import Sidebar from "./components/profile/Sidebar"
import { Outlet } from "react-router"
import ProfileHeader from "./components/profile/ProfileHeader"

const UserPanel = () => {
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

export default UserPanel
