import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const useShiftAssignmentsStore = create(

  devtools((set) => ({

    assignees: [],
    isLoading: false,

    assignEmployees: async (formData, shiftId) => {
      try {
        set({ isLoading: true });
        const { data } = await api.post(`/shifts/${shiftId}/shift-assignments`, formData);
        set(() => ({ assignees: [...data.assignees] }));
        // navigate("/login")
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    getShiftAssignees: async (shiftId) => {
      try {
        set({ isLoading: true });
        const { data } = await api.post(`/shifts/${shiftId}/shift-assignees`);
        set(() => ({ assignees: [...data.assignees] }));
        // navigate("/login")
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

  })),
);

export default useShiftAssignmentsStore;
