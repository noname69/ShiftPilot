import useNotificationStore from "../../../store/notificationStore";

const NotificationPanel = () => {
    const { notifications, markAsRead } = useNotificationStore((state) => state);

    const recent = notifications.slice(0, 5);

    const formatDate = (dateStr) => {
        const date = new Date(dateStr);
        return date.toLocaleDateString("en-GB", { day: "numeric", month: "short", hour: "2-digit", minute: "2-digit" });
    };

    return (
        <div className="absolute right-0 top-11 w-80 bg-white border border-ink-200 rounded-xl shadow-pop z-50 overflow-hidden">
            <div className="px-4 py-3 border-b border-ink-200">
                <p className="text-[13px] font-semibold text-ink-800">Notifications</p>
            </div>

            {recent.length === 0 ? (
                <div className="px-4 py-6 text-center text-[12.5px] text-ink-400">
                    No notifications
                </div>
            ) : (
                <ul>
                    {recent.map((n) => (
                        <li
                            key={n.id}
                            onClick={() => !n.isRead && markAsRead(n.id)}
                            className={`flex items-start gap-3 px-4 py-3 border-b border-ink-100 cursor-pointer hover:bg-ink-50 transition-colors ${!n.isRead ? "bg-accent-soft" : ""}`}
                        >
                            <div className="flex-1 min-w-0">
                                <p className={`text-[12.5px] truncate ${!n.isRead ? "font-semibold text-ink-800" : "text-ink-600"}`}>
                                    {n.title}
                                </p>
                                <p className="text-[11.5px] text-ink-400 mt-0.5 leading-snug">{n.message}</p>
                                <p className="text-[11px] text-ink-300 mt-1">{formatDate(n.createdAt)}</p>
                            </div>
                            {!n.isRead && (
                                <span className="mt-1 w-2 h-2 rounded-full bg-accent shrink-0"></span>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default NotificationPanel;
