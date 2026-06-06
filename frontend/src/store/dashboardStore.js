import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { getManagerDashboard, getEmployeeDashboard } from "../api/dashboard";

const useDashboardStore = create(
    devtools((set) => ({
        managerData: null,
        employeeData: null,
        isLoading: false,

        fetchManagerDashboard: async (weekStart, weekEnd) => {
            set({ isLoading: true });
            try {
                const data = await getManagerDashboard(weekStart, weekEnd);
                set({ managerData: data, isLoading: false });
            } catch (error) {
                set({ isLoading: false });
                console.error(error);
            }
        },

        fetchEmployeeDashboard: async (weekStart, weekEnd) => {
            set({ isLoading: true });
            try {
                const data = await getEmployeeDashboard(weekStart, weekEnd);
                set({ employeeData: data, isLoading: false });
            } catch (error) {
                set({ isLoading: false });
                console.error(error);
            }
        },
    }))
);

export default useDashboardStore;
