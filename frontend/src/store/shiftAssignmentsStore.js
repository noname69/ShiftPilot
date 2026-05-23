import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const useShiftAssignmentsStore = create(

  devtools((set) => ({

    assignees: [],
    isLoading: false,

    assignEmployees: async (formData, shiftId, navigate) => {
      try {
        set({ isLoading: true });
        const { data } = await api.post(`/shifts/${shiftId}/shift-assignments`, formData);
        set((state) => ({
          assignees: [...state.assignees, ...data.assignees]
        }));
        navigate("/manager/shifts");
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    getShiftAssignees: async (shiftId) => {
      try {
        set({ isLoading: true });
        const { data } = await api.get(`/shifts/${shiftId}/shift-assignees`);
        set(() => ({ assignees: [...data.assignees] }));
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

  })),
);

export default useShiftAssignmentsStore;
