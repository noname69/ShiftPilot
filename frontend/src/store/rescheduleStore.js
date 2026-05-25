import { create } from "zustand";
import { devtools } from "zustand/middleware";

import {
  createRescheduleRequest,
  getMyRescheduleRequests,
  getAllRescheduleRequests,
} from "../api/reschedule";

const useRescheduleStore = create(
  devtools((set) => ({
    isLoading: false,
    error: null,

    sendRescheduleRequest: async (payload) => {
      set({
        isLoading: true,
        error: null,
      });

      try {
        const response = await createRescheduleRequest(payload);

        set({
          isLoading: false,
        });

        return {
          success: true,
          data: response,
        };
      } catch (error) {
        const message =
          error?.response?.data?.message || "Failed to send reschedule request";

        set({
          isLoading: false,
          error: message,
        });

        return {
          success: false,
          message,
          status: error?.response?.status,
        };
      }
    },

    fetchMyRequests: async () => {
      set({ isLoading: true, error: null });

      try {
        const data = await getMyRescheduleRequests();

        set({
          requests: Array.isArray(data) ? data : [],
          isLoading: false,
        });

        return data;
      } catch (error) {
        const message =
          error?.response?.data?.message || "Failed to load requests";

        set({
          error: message,
          isLoading: false,
        });

        return [];
      }
    },

    // =========================
    // FETCH ALL (MANAGER, ADMIN)
    // =========================
    fetchAllRequests: async () => {
      set({ isLoading: true, error: null });

      try {
        const data = await getAllRescheduleRequests();

        set({
          requests: data,
          isLoading: false,
        });
      } catch (error) {
        set({
          error: error?.message || "Failed to load requests",
          isLoading: false,
        });
      }
    },
  })),
);

export default useRescheduleStore;
