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

    loginUser: async (formData, navigate) => {
      try {
        const { data } = await api.post(`/auth/login`, formData);
        set(() => ({ user: { ...data } }));
        console.log(data);
        navigate("/user");
      } catch (error) {
        console.log(error);
      }
    },
  })),
);

export default useAuthStore;
