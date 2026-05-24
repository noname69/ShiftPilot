import { create } from "zustand";
import { devtools } from "zustand/middleware";

import { createRescheduleRequest } from "../api/reschedule";

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
          error?.response?.data?.message ||
          "Failed to send reschedule request";

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
  })),
);

export default useRescheduleStore;