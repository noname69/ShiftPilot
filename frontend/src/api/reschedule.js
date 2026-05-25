import api from "./api";

export const createRescheduleRequest = async (data) => {
  const response = await api.post("/swap-requests", data);

  return response.data;
};

export const getMyRescheduleRequests = async () => {
  const response = await api.get("/swap-requests/my");
  return response.data;
};

export const getAllRescheduleRequests = async () => {
  const response = await api.get("/swap-requests/all");
  return response.data;
};

// Target user response
export const respondAsTarget = async (id, accepted) => {
  const response = await api.patch(
    `/swap-requests/${id}/target-response`,
    { accepted }
  );
  return response.data;
};

// Manager response
export const respondAsManager = async (id, approved) => {
  const response = await api.patch(
    `/swap-requests/${id}/manager-response`,
    { approved }
  );
  return response.data;
};