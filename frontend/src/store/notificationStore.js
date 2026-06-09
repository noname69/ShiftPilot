import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { getNotifications, markNotificationAsRead } from "../api/notification";

const useNotificationStore = create(
    devtools((set) => ({
        notifications: [],
        unreadCount: 0,
        isLoading: false,
        totalPages: 0,
        currentPage: 0,

        fetchNotifications: async (page) => {
            set({ isLoading: true });
            try {
                const data = await getNotifications(page);
                set({
                    notifications: data.content,
                    totalPages: data.totalPages,
                    currentPage: data.number,
                    unreadCount: data.content.filter((n) => !n.isRead).length,
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
