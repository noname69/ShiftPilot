import api from "./api";

export const getManagerDashboard = async (weekStart, weekEnd) => {
    const response = await api.get("/dashboard/manager", { params: { weekStart, weekEnd } });
    return response.data;
};

export const getEmployeeDashboard = async (weekStart, weekEnd) => {
    const response = await api.get("/dashboard/me", { params: { weekStart, weekEnd } });
    return response.data;
};

export const processManagerRequest = async (body) => {
    const response = await api.post("/managers/me/process-request", body);
    return response.data;
};

export const respondAsSwapTarget = async (swapRequestId, accepted) => {
    const response = await api.patch("/swap-requests/target/respond", { swapRequestId, accepted, comment: null });
    return response.data;
};
