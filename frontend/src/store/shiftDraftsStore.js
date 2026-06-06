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

    createShiftDraft: async (formData, navigate, role) => {
      try {
        set({ isLoading: true });
        await api.post(`/shift-drafts`, formData);
        toast.success("Draft created successfully")
        navigate(`/${role}/shifts`)
      } catch (error) {
        console.log(error);
        const msg = error?.response?.data?.message || error.message;
        toast.error(msg);
      } finally {
        set({ isLoading: false });
      }
    },

    removeDraft: async (draftId, navigate, role) => {
      try {
        console.log(draftId)
        set({ isLoading: true });
        await api.delete(`/shift-drafts/${draftId}`);
        set((state) => ({
          drafts: state.drafts.filter(draft => draft.id !== draftId)
        }));
        navigate(`/${role}/shifts`)
        toast.success("Draft deleted successfully")
      } catch (error) {
        console.log(error);
        const msg = error?.response?.data?.message || error.message;
        toast.error(msg);
      } finally {
        set({ isLoading: false });
      }
    },

  })),
);

export default useShiftDraftsStore;
