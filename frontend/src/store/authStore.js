import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";
import toast from "react-hot-toast";

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

    loginUser: async (formData, navigate, reset) => {
      try {
        set({ isLoading: true })
        const { data } = await api.post(`/auth/login`, formData);
        set(() => ({ user: { ...data } }));
        navigate("/login")
        toast.success("Logged in successfully")
      } catch (error) {
        reset()
        console.error(error);
        toast.error(error.response.data.message);
      } finally {
        set({ isLoading: false })
      }
    },

    // registerUser: async (formData, navigate) => {
    //   try {
    //     await api.post(`/users`, formData);
    //     navigate("/");
    //     toast.success("User registered successfully")
    //   } catch (error) {
    //     console.log(error);
    //     toast.error(error.response.data.message);
    //   }
    // },

    logoutUser: async (navigate) => {
      try {
        set({ isLoading: true })
        await api.post(`/auth/logout`);
        set(() => ({ user: initialState }))
        navigate("/login");
        toast.success("Logged out successfully")
      } catch (error) {
        console.log(error);
        toast.error(error.response.data.message);
      } finally {
        set({ isLoading: false })
      }
    },

    fetchCurrentUser: async () => {
      try {
        set({ isLoading: true })
        const { data } = await api.get("/auth/me");
        set(() => ({ user: data }));
        set({ authMeIsLoading: false });
      } catch {
        set({ user: initialState });
        set({ authMeIsLoading: false });
      } finally {
        set({ isLoading: false })
      }
    },

  })),
);

export default useAuthStore;
