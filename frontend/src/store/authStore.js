import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const initialState = {
  username: null,
  userId: null,
  role: "",
};

const useAuthStore = create(
  devtools((set) => ({
    user: initialState,
    isLoading: false,
    authMeIsLoading: true,
    authError: null,

    loginUser: async (formData, navigate) => {
      try {
        set({ authError: null });

        const { data } = await api.post(`/auth/login`, formData);
        set(() => ({ user: { ...data }, authError: null }));

        navigate("/");
      } catch (error) {
        const message =
          error?.response?.data?.message || "Invalid credentials";
        set({ authError: message });
      }
    },

    registerUser: async (formData, navigate) => {
      try {
        await api.post(`/users`, formData);
        navigate("/");
      } catch (error) {
        console.log(error);
      }
    },

    logoutUser: async (navigate) => {
      try {
        await api.post(`/auth/logout`);
        set(() => ({ user: initialState }));
        navigate("/login");
      } catch (error) {
        console.log(error);
      }
    },

    fetchCurrentUser: async () => {
      try {
        set({ isLoading: true });
        const { data } = await api.get("/auth/me");
        set(() => ({ user: data }));
        set({ authMeIsLoading: false });
      } catch {
        set({ user: initialState });
        set({ authMeIsLoading: false });
      } finally {
        set({ isLoading: false });
      }
    },
  })),
);

export default useAuthStore;
