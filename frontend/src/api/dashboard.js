import api from "./api";

export const getManagerDashboard = async () => {
    const response = await api.get("/dashboard/manager");
    return response.data;
};

export const getEmployeeDashboard = async (weekStart, weekEnd) => {
    const response = await api.get("/dashboard/me", { params: { weekStart, weekEnd } });
    return response.data;
};
