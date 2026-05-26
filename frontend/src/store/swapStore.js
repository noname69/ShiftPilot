import { create } from "zustand";
import { devtools } from "zustand/middleware";

import {
  createSwapRequest,
  getMySwapRequests,
  getAllSwapRequests,
} from "../api/swap";

const useSwapStore = create(
  devtools((set) => ({
    isLoading: false,
    error: null,

    sendSwapRequest: async (payload) => {
      set({
        isLoading: true,
        error: null,
      });

      try {
        const response = await createSwapRequest(payload);

        set({
          isLoading: false,
        });

        return {
          success: true,
          data: response,
        };
      } catch (error) {
        const message =
          error?.response?.data?.message || "Failed to send swap request";

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
        const data = await getMySwapRequests();

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
        const data = await getAllSwapRequests();

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

export default useSwapStore;
