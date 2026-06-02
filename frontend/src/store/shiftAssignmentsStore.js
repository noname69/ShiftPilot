import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";
import toast from "react-hot-toast";

const useShiftAssignmentsStore = create(

  devtools((set) => ({

    assignees: [],
    overlappingUserIds: [],
    isLoading: false,

    assignEmployees: async (formData, shiftId, navigate) => {
      try {
        set({ isLoading: true });
        const { data } = await api.post(`/shifts/${shiftId}/shift-assignments`, formData);
        set((state) => ({
          assignees: [...state.assignees, ...data.assignees]
        }));
        navigate("/manager/shifts");
        toast.success("User assigned to shift successfully")
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
        set(() => ({ overlappingUserIds: [...data.overlappingUserIds] }));
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    removeAssignment: async (shiftId, userId) => {
      try {
        set({ isLoading: true });
        await api.patch(`/shifts/${shiftId}/shift-assignments/${userId}/remove`);
        set((state) => ({
          assignees: state.assignees.map((a) =>
            a.id === userId ? { ...a, status: "REMOVED" } : a
          ),
        }));
      } catch (error) {
        console.log(error);
        throw error;
      } finally {
        set({ isLoading: false });
      }
    },

    removeEmployeeFromShiftAssignment: async (userId, shiftId) => {
      try {
        set({ isLoading: true });
        await api.delete(`/users/${userId}/shifts/${shiftId}`);
        set((state) => ({
          assignees: state.assignees.filter((a) => a.id !== userId),
        }));
        toast.success("User removed successfully")
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

  })),
);

export default useShiftAssignmentsStore;
