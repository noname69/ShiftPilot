import api from "./api";

export const getNotifications = async (page) => {
    const response = await api.get("/notifications/me", {params: page});
    return response.data;
};

export const markNotificationAsRead = async (id) => {
    await api.patch(`/notifications/${id}/read`);
};
