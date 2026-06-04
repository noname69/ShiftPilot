import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";
import toast from "react-hot-toast";

const useShiftDraftsStore = create(

  devtools((set) => ({

    drafts: [],
    isLoading: false,

    fetchShiftDrafts: async () => {
      try {
        set({ isLoading: true });
        const { data } = await api.get(`/shift-drafts`);
        set(() => ({ drafts: [...data] }));
      } catch (error) {
        console.log(error);
      } finally {
        set({ isLoading: false });
      }
    },

    createShiftDraft: async (formData) => {
      try {
        set({ isLoading: true });
        await api.post(`/shift-drafts`, formData);
        toast.success("Draft created successfully")
      } catch (error) {
        console.log(error);
        toast.error(error)
      } finally {
        set({ isLoading: false });
      }
    },

  })),
);

export default useShiftDraftsStore;
