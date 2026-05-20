import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const initialState = {
  "username": null,
  "userId": null,
  "role": ""
}

const useUsersStore = create(

  devtools((set) => ({

    user: initialState,
    isLoading: false,

    loginUser: async (formData, navigate) => {
      try {
        const { data } = await api.post(`/auth/login`, formData);
        set(() => ({ user: { ...data } }))
        const { role } = data;
        console.log(role)
        switch (role) {
          case "USER": {
            navigate("/user")
            break
          }
          case "MANAGER": {
            navigate("/manager")
            break
          }
          case "ADMIN": {
            navigate("/admin")
            break
          }
          default:
            navigate("/login")
        }

      } catch (error) {
        console.log(error);
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
      console.log("logout")
      try {
        await api.post(`/auth/logout`);
        set(() => ({ user: initialState }))
        navigate("/");
      } catch (error) {
        console.log(error);
      }
    },

    fetchCurrentUser: async () => {
      try {
        set({ isLoading: true })
        const { data } = await api.get("/auth/me");
        set(() => ({ user: data }));
      } catch {
        set({ user: initialState });
      } finally {
        set({ isLoading: false })
      }
    },

  })),
);

export default useUsersStore;