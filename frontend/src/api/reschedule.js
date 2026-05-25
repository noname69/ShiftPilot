import api from "./api";

export const createRescheduleRequest = async (data) => {
  const response = await api.post("/reschedule-requests", data);

  return response.data;
};

export const getMyRescheduleRequests = async () => {
  const response = await api.get("/reschedule-requests/my");
  return response.data;
};

export const getAllRescheduleRequests = async () => {
  const response = await api.get("/reschedule-requests/all");
  return response.data;
};

// Target user response
export const respondAsTarget = async (id, accepted) => {
  const response = await api.patch(
    `/reschedule-requests/${id}/target-response`,
    { accepted }
  );
  return response.data;
};

// Manager response
export const respondAsManager = async (id, approved) => {
  const response = await api.patch(
    `/reschedule-requests/${id}/manager-response`,
    { approved }
  );
  return response.data;
};