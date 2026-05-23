import api from "./api";

export const createShift = async (data) => {
  console.log(data)
  const response = await api.post("/shifts", data);
  return response.data;
};

export const updateShift = async (id, data) => {
  const response = await api.put(`/shifts/${id}`, data);
  return response.data;
};

export const getShifts = async () => {
  const response = await api.get("/shifts");
  return response.data;
};

export const getShiftById = async (id) => {
  const response = await api.get(`/shifts/${id}`);
  return response.data;
};

export const deleteShift = async (id) => {
  await api.delete(`/shifts/${id}`);
};

export const getUserShifts = async () => {
  const response = await api.get(`/users/me/shifts`);
  return response.data;
}
