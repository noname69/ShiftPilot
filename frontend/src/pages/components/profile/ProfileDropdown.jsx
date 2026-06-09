import { Menu } from "@headlessui/react";
import { CgProfile } from "react-icons/cg";
import { IoSettingsOutline } from "react-icons/io5";
import { IoMdExit } from "react-icons/io";
import { useEffect } from "react";
import { useNavigate, Link } from "react-router";
import useAuthStore from "../../../store/authStore";

export default function ProfileDropdown() {
  const { user, logoutUser } = useAuthStore((state) => state);
  const role = user?.role;

  const navigate = useNavigate();

  const userPath =
    user.role === "USER"
      ? "/user"
      : user.role === "MANAGER"
        ? "/manager"
        : user.role === "ADMIN" && "/admin";

  const handleLogout = () => {
    logoutUser(navigate);
  };

  const handleNavigate = () => {
    navigate(userPath);
  };

  useEffect(() => {}, [user]);

  return (
    <Menu as="div" className="relative inline-block text-left z-11">
      <Menu.Button className="w-9 h-9 rounded-full bg-linear-to-br from-ink-700 to-ink-900 text-white flex items-center justify-center text-[12px] font-semibold">
        <div>
          {user.firstName?.[0].toUpperCase()}
          {user.lastName?.[0].toUpperCase()}
        </div>
      </Menu.Button>

      <Menu.Items className="absolute right-0 mt-2 w-48 bg-white border border-ink-200 shadow-lg rounded-lg p-2 flex flex-col gap-4 outline-none text-[13px]">
        <Menu.Item>
          <div
            className="flex items-center gap-2 cursor-pointer"
            onClick={handleNavigate}
          >
            <CgProfile />
            <p>My Profile</p>
          </div>
        </Menu.Item>

        <Link to={`/${role}/edit`}>
          <Menu.Item>
            <div className="flex items-center gap-2 cursor-pointer">
              <IoSettingsOutline />
              <p>Settings</p>
            </div>
          </Menu.Item>
        </Link>

        <Menu.Item>
          <div
            className="flex items-center gap-2 cursor-pointer"
            onClick={handleLogout}
          >
            <IoMdExit />
            <p>Logout</p>
          </div>
        </Menu.Item>
      </Menu.Items>
    </Menu>
  );
}
