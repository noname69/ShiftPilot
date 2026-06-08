import api from "./api";

export const createShift = async (data) => {
  const response = await api.post("/shifts", data);
  return response.data;
};

export const updateShift = async (id, data) => {
  const response = await api.put(`/shifts/${id}`, data);
  return response.data;
};

export const getShifts = async (filters) => {
  const response = await api.get("/shifts", {params: filters});
  return response.data;
};

export const getShiftById = async (id) => {
  const response = await api.get(`/shifts/${id}`);
  return response.data;
};

export const cancelShift = async (id) => {
  await api.patch(`/shifts/${id}`);
};

export const getUserShifts = async (filters) => {
  const response = await api.get(`/users/me/shifts`, {params: filters});
  return response.data;
}

export const getUserWeeklyShifts = async (filters) => {
  const response = await api.get("/shift-assignments/me/schedule", {params: filters});
  return response.data;
}

export const getAllUsersWeeklyShifts = async (filters) => {
  const response = await api.get("/shift-assignments/schedule", {params: filters});
  return response.data;
}