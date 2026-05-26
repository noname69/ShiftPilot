import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { createShift, updateShift, getShifts, cancelShift, getUserShifts } from "../api/shift";

const useShiftStore = create(
  devtools((set) => ({
    shifts: [],
    isLoading: false,
    error: null,
    userShifts: [],

    fetchShifts: async () => {
      set({ isLoading: true, error: null });
      try {
        const data = await getShifts();
        set({ shifts: data, isLoading: false });
      } catch (error) {
        set({ error: error.message, isLoading: false });
        console.error(error);
      }
    },

    addShift: async (formData) => {
      set({ isLoading: true, error: null });
      try {
        await createShift(formData);
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
        console.log(data)
        set({ userShifts: data });
      } catch (error) {
        set({ error: error.message });
        console.error(error);
      } finally{
        set({ isLoading: false })
      }
    },

  }))
);

export default useShiftStore;