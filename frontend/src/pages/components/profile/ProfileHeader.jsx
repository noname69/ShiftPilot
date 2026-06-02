import { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router";
import ProfileDropdown from "./ProfileDropdown";
import NotificationPanel from "./NotificationPanel";
import useNotificationStore from "../../../store/notificationStore";

const ProfileHeader = () => {
  const location = useLocation();
  const name = location.state?.name || "Dashboard";

  const [panelOpen, setPanelOpen] = useState(false);
  const panelRef = useRef(null);

  const { unreadCount, fetchNotifications } = useNotificationStore((state) => state);

  useEffect(() => {
    fetchNotifications();
  }, []);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (panelRef.current && !panelRef.current.contains(e.target)) {
        setPanelOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <header className="sticky top-0 z-30 border-b border-ink-200 bg-ink-50/80 backdrop-blur-md">
      <div className="flex items-center gap-3 px-5 lg:px-8 h-14">
        <button className="lg:hidden text-ink-700 -ml-1">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><path d="M3 6h18M3 12h18M3 18h18"></path></svg>
        </button>

        <nav className="text-[13px] text-ink-500 flex items-center gap-2 min-w-0">
          <span className="text-ink-400">Workspace</span>
          <span className="text-ink-300">/</span>
          <span id="crumb" className="text-ink-800 font-medium truncate">{name}</span>
        </nav>

        <div className="ml-auto flex items-center gap-4">

          <div className="hidden md:flex items-center gap-2 text-[12.5px] text-ink-500 px-2.5 py-1 rounded-md border border-ink-200 bg-white">
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><rect x="3" y="4" width="18" height="17" rx="2"></rect><path d="M3 9h18"></path></svg>
            Week of May 18 – 24
          </div>

          <div className="relative" ref={panelRef}>
            <button
              id="notif-btn"
              onClick={() => setPanelOpen((prev) => !prev)}
              className="relative w-9 h-9 rounded-md border border-ink-200 bg-white hover:bg-ink-50 flex items-center justify-center"
            >
              <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8"><path d="M18 8a6 6 0 1 0-12 0c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
              {unreadCount > 0 && (
                <span className="absolute top-1.5 right-1.5 w-1.5 h-1.5 rounded-full bg-rose-ink"></span>
              )}
            </button>

            {panelOpen && <NotificationPanel />}
          </div>

          <ProfileDropdown />
        </div>
      </div>
    </header>
  );
};

export default ProfileHeader;
