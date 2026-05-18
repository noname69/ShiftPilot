import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";

const initialState = {
  "username": null,
  "userId": null,
  "roles": []
}

const useUsersStore = create(

  devtools(() => ({

    user: initialState,
    isLoading: false,

    registerUser: async (formData, navigate) => {
      try {
        await api.post(`/users`, formData);
        navigate("/");
      } catch (error) {
        console.log(error);
      }
    },

  })),
);

export default useUsersStore;