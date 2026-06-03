import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { createShift, updateShift, getShifts, cancelShift, getUserShifts, getShiftById, getDraftedShifts } from "../api/shift";

const useShiftStore = create(
  devtools((set) => ({
    shifts: [],
    draftedShifts: [],
    shift: null,
    isLoading: false,
    error: null,
    userShifts: [],
    totalPages: 0,
    currentPage: 0,

    fetchShifts: async (filters) => {
      set({ isLoading: true, error: null });
      try {
        const data = await getShifts(filters);
        set({
          shifts: data.content,
          totalPages: data.totalPages,
          currentPage: data.number,
          isLoading: false
        });
      } catch (error) {
        set({ error: error.message, isLoading: false });
        console.error(error);
      }
    },

    fetchShiftById: async (id) => {
      set({ isLoading: true, error: null });
      try {
        const data = await getShiftById(id);
        set({ shift: data, isLoading: false });
        return data;
      } catch (error) {
        set({ error: error.message, isLoading: false });
        console.error(error);
      }
    },

    addShift: async (formData) => {
      console.log(formData)
      set({ isLoading: true, error: null });
      try {
        const data = await createShift(formData);
        console.log(data)
        set({ isLoading: false });
        return true;
      } catch (error) {
        set({ error: error.message, isLoading: false });
        console.error(error);
        return false;
      }
    },

    editShift: async (id, formData) => {
      set({ isLoading: true, error: null });
      try {
        await updateShift(id, formData);
        set({ isLoading: false });
        return true;
      } catch (error) {
        set({ error: error.message, isLoading: false });
        console.error(error);
        return false;
      }
    },

    removeShift: async (id) => {
      await cancelShift(id);
      set((state) => ({
        shifts: state.shifts.map((s) =>
          s.id === id ? { ...s, status: "CANCELLED" } : s
        ),
      }));
    },

    fetchUserShifts: async () => {
      try {
        set({ isLoading: true, error: null });
        const data = await getUserShifts();
        set({ userShifts: data });
      } catch (error) {
        set({ error: error.message });
        console.error(error);
      } finally {
        set({ isLoading: false })
      }
    },

    fetchDraftedShifts: async () => {
      try {
        set({ isLoading: true, error: null });
        const data = await getDraftedShifts();
        set({ draftedShifts: data });
      } catch (error) {
        set({ error: error.message });
        console.error(error);
      } finally {
        set({ isLoading: false })
      }
    },

  }))
);

export default useShiftStore;