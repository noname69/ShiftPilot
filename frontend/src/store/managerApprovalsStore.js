import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const useManagerApprovalsStore = create(

  devtools((set) => ({

    requests: [],
    isLoading: false,
    totalPages: 0,
    currentPage: 0,

    fetchManagerApprovals: async (filters) => {
      try {
        set({ isLoading: true });
        const { data } = await api.get(`/managers/me/manager-approvals`, { params: filters });
        set({
          requests: data.content,
          totalPages: data.totalPages,
          currentPage: data.number,
        });
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    fetchUserRequests: async (filters) => {
      try {
        set({ isLoading: true });
        const { data } = await api.get(`/users/me/my-requests`, { params: filters });
        set({
          requests: data.content,
          totalPages: data.totalPages,
          currentPage: data.number,
        });
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    processRequest: async (sendData) => {
      console.log(sendData)
      try {
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
