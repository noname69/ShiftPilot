import { create } from "zustand";
import { devtools } from "zustand/middleware";

import {
  createUser,
  getUsers,
  updateUser,
  deleteUser,
  restoreUser,
  searchUsers,
  editPersonalInfo
} from "../api/user";
import toast from "react-hot-toast";

const useUserStore = create(
  devtools((set) => ({
    users: [],
    isLoading: false,
    error: null,
    totalPages: 0,
    currentPage: 0,
    totalElements: 0,

    fetchUsers: async () => {
      set({ isLoading: true, error: null });

      try {
        const res = await getUsers();
        const users = res?.data ?? res;

        set({
          users,
          isLoading: false,
        });
      } catch (error) {
        set({
          error: error.message,
          isLoading: false,
        });
      }
    },

    searchUsers: async (filters) => {
      set({ isLoading: true, error: null });

      try {
        const data = await searchUsers(filters);

        set({
          users: data.content,
          totalPages: data.totalPages,
          currentPage: data.number,
          totalElements: data.totalElements,
          isLoading: false,
        });
      } catch (error) {
        set({
          error: error.message,
          isLoading: false,
        });

        console.error(error);
      }
    },

    addUser: async (formData, navigate, role) => {
      set({ isLoading: true, error: null });
      try {
        await createUser(formData);
        set({ isLoading: false });
        navigate(`/${role}/users`);
        toast.success("User created successfully");

      } catch (error) {
        set({ isLoading: false });
        console.log(error);
        const msg = error?.response?.data?.message || error.message || "Failed to create a user";
        toast.error(msg);
      }
    },

    editUser: async (id, formData, navigate, role) => {
      set({ isLoading: true, error: null });
      try {
        await updateUser(id, formData);
        set({ isLoading: false });
        navigate(`/${role}/users`);
        toast.success("User updated successfully");
      } catch (error) {
        set({ isLoading: false });
        const message =
          error?.response?.data?.message || "Failed to update user";
        toast.error(message);
      }
    },

    editPersonalInformation: async (formData, navigate, role, fetchCurrentUser) => {
      set({ isLoading: true, error: null });
      try {
        await editPersonalInfo(formData);
        set({ isLoading: false });
        fetchCurrentUser();
        navigate(`/${role}`);
        toast.success("Information updated successfully");
      } catch (error) {
        set({ isLoading: false });
        const message =
          error?.response?.data?.message || "Failed to update user";
        toast.error(message);
      }
    },

    removeUser: async (id) => {
      try {
        await deleteUser(id);

        set((state) => ({
          users: state.users.map((user) =>
            user.id === id ? { ...user, status: "INACTIVE" } : user,
          ),
        }));
        toast.success("User removed successfully");
      } catch (error) {
        console.error(error);
        const msg = error?.response?.data?.message || error.message || "Failed to remove user";
        toast.error(msg);
      }
    },

    makeActive: async (id) => {
      try {
        await restoreUser(id);
        set((state) => ({
          users: state.users.map((user) =>
            user.id === id ? { ...user, status: "ACTIVE" } : user,
          ),
        }));
        toast.success("User restored successfully");
      } catch (error) {
        const msg = error?.response?.data?.message || error.message;
        toast.error(msg);
      }
    },
  })),
);

export default useUserStore;
