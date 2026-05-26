import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const useManagerApprovalsStore = create(

  devtools((set) => ({

    requests: [],
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

    removeAssignment: async (shiftId, userId) => {

      console.log(userId)
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

  })),
);

export default useManagerApprovalsStore;
