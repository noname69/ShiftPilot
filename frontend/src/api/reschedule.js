import api from "./api";

export const createRescheduleRequest = async (data) => {
  const response = await api.post("/reschedule-requests", data);

  return response.data;
};