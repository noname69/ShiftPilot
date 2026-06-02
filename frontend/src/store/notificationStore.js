import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { getNotifications, markNotificationAsRead } from "../api/notification";

const useNotificationStore = create(
    devtools((set) => ({
        notifications: [],
        unreadCount: 0,
        isLoading: false,

        fetchNotifications: async () => {
            set({ isLoading: true });
            try {
                const data = await getNotifications();
                set({
                    notifications: data,
                    unreadCount: data.filter((n) => !n.isRead).length,
                    isLoading: false,
                });
            } catch (error) {
                set({ isLoading: false });
                console.error(error);
            }
        },

        markAsRead: async (id) => {
            try {
                await markNotificationAsRead(id);
                set((state) => ({
                    notifications: state.notifications.map((n) =>
                        n.id === id ? { ...n, isRead: true } : n
                    ),
                    unreadCount: Math.max(0, state.unreadCount - 1),
                }));
            } catch (error) {
                console.error(error);
            }
        },
    }))
);

export default useNotificationStore;
