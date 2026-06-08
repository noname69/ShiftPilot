import { useEffect, useState } from "react";
import { FaCheck, FaTimes } from "react-icons/fa";
import toast from "react-hot-toast";
import useNotificationStore from "../../../store/notificationStore";
import useAuthStore from "../../../store/authStore";
import { respondAsTarget, managerRespondFromNotification } from "../../../api/swap";

const NotificationPanel = () => {
    const { notifications, markAsRead, fetchNotifications } = useNotificationStore((state) => state);
    const role = useAuthStore((state) => state.user?.role);
    const [loadingId, setLoadingId] = useState(null);

    useEffect(() => {
        fetchNotifications();
    }, []);

    const recent = notifications.slice(0, 5);

    const isActionable = (n) => n.type === "REQUEST_SUBMITTED" && n.referenceId != null;

    const handleTargetRespond = async (n, accepted) => {
        setLoadingId(n.id);
        try {
            await respondAsTarget({ swapRequestId: n.referenceId, accepted, comment: null });
            await markAsRead(n.id);
            await fetchNotifications();
            toast.success(accepted ? "Swap request accepted" : "Swap request declined");
        } catch {
            toast.error("Failed to respond to swap request");
        } finally {
            setLoadingId(null);
        }
    };

    const handleManagerRespond = async (n, decision) => {
        setLoadingId(n.id);
        try {
            await managerRespondFromNotification(n.referenceId, decision);
            await markAsRead(n.id);
            await fetchNotifications();
            toast.success(decision ? "Swap request approved" : "Swap request rejected");
        } catch {
            toast.error("Failed to process swap request");
        } finally {
            setLoadingId(null);
        }
    };

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
                    {recent.map((n) => {
                        const actionable = isActionable(n);
                        const isUser = role === "USER";
                        const isManager = role === "MANAGER" || role === "ADMIN";
                        const loading = loadingId === n.id;

                        return (
                            <li
                                key={n.id}
                                onClick={() => !actionable && !n.isRead && markAsRead(n.id)}
                                className={`px-4 py-3 border-b border-ink-100 transition-colors
                                    ${!n.isRead ? "bg-accent-soft" : ""}
                                    ${!actionable ? "cursor-pointer hover:bg-ink-50" : ""}`}
                            >
                                <div className="flex items-start gap-3">
                                    <div className="flex-1 min-w-0">
                                        <p className={`text-[12.5px] truncate ${!n.isRead ? "font-semibold text-ink-800" : "text-ink-600"}`}>
                                            {n.title}
                                        </p>
                                        <p className="text-[11.5px] text-ink-400 mt-0.5 leading-snug whitespace-pre-line">
                                            {n.message}
                                        </p>
                                        <p className="text-[11px] text-ink-300 mt-1">{formatDate(n.createdAt)}</p>
                                    </div>
                                    {!n.isRead && !actionable && (
                                        <span className="mt-1 w-2 h-2 rounded-full bg-accent shrink-0" />
                                    )}
                                </div>

                                {actionable && (
                                    <div className="flex gap-2 mt-2.5">
                                        {isUser && (
                                            <>
                                                <button
                                                    disabled={loading}
                                                    onClick={(e) => { e.stopPropagation(); handleTargetRespond(n, true); }}
                                                    className="flex-1 flex items-center justify-center gap-1.5 text-[11.5px] font-medium bg-mint-soft text-mint-ink hover:bg-mint-soft/80 border border-mint-ink/20 py-1.5 rounded-md transition-colors disabled:opacity-50"
                                                >
                                                    <FaCheck size={10} /> Accept
                                                </button>
                                                <button
                                                    disabled={loading}
                                                    onClick={(e) => { e.stopPropagation(); handleTargetRespond(n, false); }}
                                                    className="flex-1 flex items-center justify-center gap-1.5 text-[11.5px] font-medium bg-rose-soft text-rose-ink hover:bg-rose-soft/80 border border-rose-ink/20 py-1.5 rounded-md transition-colors disabled:opacity-50"
                                                >
                                                    <FaTimes size={10} /> Decline
                                                </button>
                                            </>
                                        )}
                                        {isManager && (
                                            <>
                                                <button
                                                    disabled={loading}
                                                    onClick={(e) => { e.stopPropagation(); handleManagerRespond(n, true); }}
                                                    className="flex-1 flex items-center justify-center gap-1.5 text-[11.5px] font-medium bg-mint-soft text-mint-ink hover:bg-mint-soft/80 border border-mint-ink/20 py-1.5 rounded-md transition-colors disabled:opacity-50"
                                                >
                                                    <FaCheck size={10} /> Approve
                                                </button>
                                                <button
                                                    disabled={loading}
                                                    onClick={(e) => { e.stopPropagation(); handleManagerRespond(n, false); }}
                                                    className="flex-1 flex items-center justify-center gap-1.5 text-[11.5px] font-medium bg-rose-soft text-rose-ink hover:bg-rose-soft/80 border border-rose-ink/20 py-1.5 rounded-md transition-colors disabled:opacity-50"
                                                >
                                                    <FaTimes size={10} /> Reject
                                                </button>
                                            </>
                                        )}
                                    </div>
                                )}
                            </li>
                        );
                    })}
                </ul>
            )}
        </div>
    );
};

export default NotificationPanel;
