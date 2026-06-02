import api from "./api";

export const getNotifications = async () => {
    const response = await api.get("/notifications/me");
    return response.data;
};

export const markNotificationAsRead = async (id) => {
    await api.patch(`/notifications/${id}/read`);
};
