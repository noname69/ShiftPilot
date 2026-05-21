import ProfileHeader from "./components/profile/ProfileHeader"
import { Outlet } from "react-router"
import Sidebar from "./components/profile/Sidebar"

const ManagerPanel = () => {
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

export default ManagerPanel
