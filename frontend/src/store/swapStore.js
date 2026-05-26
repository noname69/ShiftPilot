import { create } from "zustand";
import { devtools } from "zustand/middleware";

import {
  createSwapRequest,
  respondAsTarget,
  respondAsManager,
} from "../api/swap";

const useSwapStore = create(
  devtools((set) => ({
    isLoading: false,
    error: null,
    requests: [],

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
        console.error("Failed to send swap request", error);
        const message =
          error?.response?.data?.message || "Failed to send swap request";

        set({
          isLoading: false,
        });

        return {
          success: false,
          message,
          status: error?.response?.status,
        };
      }
    },

    sendTargetResponse: async (payload) => {
      set({ isLoading: true, error: null });

      try {
        await respondAsTarget(payload);

        set((state) => ({
          isLoading: false,
          requests: state.requests.map((r) =>
            r.requestId === payload.swapRequestId
              ? {
                  ...r,
                  approvalStatus: payload.accepted
                    ? "PENDING_MANAGER_APPROVAL"
                    : "TARGET_REJECTED",
                }
              : r,
          ),
        }));

        return { success: true };
      } catch (error) {
        set({ isLoading: false });

        return {
          success: false,
          message:
            error?.response?.data?.message || "Failed to send target response",
        };
      }
    },

    sendManagerResponse: async (payload) => {
      set({ isLoading: true, error: null });

      try {
        await respondAsManager(payload);

        set((state) => ({
          isLoading: false,
          requests: state.requests.map((r) =>
            r.requestId === payload.swapRequestId
              ? {
                  ...r,
                  approvalStatus: payload.accepted
                    ? "APPROVED"
                    : "MANAGER_REJECTED",
                }
              : r,
          ),
        }));

        return { success: true };
      } catch (error) {
        set({ isLoading: false });

        return {
          success: false,
          message:
            error?.response?.data?.message || "Failed to send manager response",
        };
      }
    },
  })),
);

export default useSwapStore;
