import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const useManagerApprovalsStore = create(

  devtools((set) => ({

    requests: [],
    isLoading: false,

    fetchManagerApprovals: async () => {
      try {
        set({ isLoading: true });
        const { data } = await api.get(`/managers/me/manager-approvals`);
        set(() => ({ requests: [...data.content] }));
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    fetchUserRequests: async () => {
      try {
        set({ isLoading: true });
        const { data } = await api.get(`/users/me/my-requests`);
        set(() => ({ requests: [...data.content] }));
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    processRequest: async (sendData) => {
      try {
        console.log(sendData)
        set({ isLoading: true });
        const { data } = await api.post(`/managers/me/process-request`, sendData);
        return data.message;
      } catch (error) {
        console.log("STATUS:", error.response?.status);
        console.log("DATA:", error.response?.data);
        console.log("MESSAGE:", error.message);
      } finally {
      set({ isLoading: false });
    }
  },

  })),
);

export default useManagerApprovalsStore;
