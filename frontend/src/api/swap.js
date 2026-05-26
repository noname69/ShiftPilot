import api from "./api";

export const createSwapRequest = async (data) => {
  const response = await api.post("/swap-requests", data);

  return response.data;
};

// export const getMySwapRequests = async () => {
//   const response = await api.get("/swap-requests/my");
//   return response.data;
// };

// export const getAllSwapRequests = async () => {
//   const response = await api.get("/swap-requests/all");
//   return response.data;
// };

// Target user response
export const respondAsTarget = async (accepted) => {
  console.log("API CALL - respondAsTarget with accepted:", accepted);
  const response = await api.patch(
    `/swap-requests/target/respond`,
    accepted
  );
  return response.data;
};

// Manager response
export const respondAsManager = async (approved) => {
  console.log("API CALL - respondAsManager with approved:", approved);
  const response = await api.patch(
    `/swap-requests/manager/respond`,
    approved
  );
  return response.data;
};