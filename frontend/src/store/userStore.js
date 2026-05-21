import { create } from "zustand";
import { devtools } from "zustand/middleware";
import {
  createUser,
  getUsers,
  updateUser,
  deleteUser,
  restoreUser,
} from "../api/user";

const useUserStore = create(
  devtools((set) => ({
    users: [],
    isLoading: false,
    error: null,

    fetchUsers: async () => {
      set({ isLoading: true, error: null });

      try {
        const res = await getUsers();
        console.log("API RESPONSE:", res);

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

    addUser: async (formData) => {
      set({ isLoading: true, error: null });
      try {
        await createUser(formData);

        set({ isLoading: false });
        return { success: true };
      } catch (error) {
        set({ isLoading: false });

        const message =
          error?.response?.data?.message || "Failed to create user";

        set({ error: message });

        return {
          success: false,
          message,
          status: error?.response?.status,
        };
      }
    },

    editUser: async (id, formData) => {
      set({ isLoading: true, error: null });
      try {
        await updateUser(id, formData);

        set({ isLoading: false });
        return { success: true };
      } catch (error) {
        set({ isLoading: false });

        const message =
          error?.response?.data?.message || "Failed to update user";

        set({ error: message });

        return {
          success: false,
          message,
          status: error?.response?.status,
        };
      }
    },

    removeUser: async (id) => {
      try {
        await deleteUser(id);

        set((state) => ({
          users: state.users.map((u) =>
            u.id === id ? { ...u, status: "INACTIVE" } : u,
          ),
        }));

        return true;
      } catch (error) {
        console.error(error);
        return false;
      }
    },

    udeleteUser: async (id) => {
      try {
        await restoreUser(id);
        set((state) => ({
          users: state.users.map((user) =>
            user.id === id ? { ...user, status: "ACTIVE" } : user,
          ),
        }));
        return true;
      } catch (error) {
        console.error(error);
        return false;
      }
    },
  })),
);

export default useUserStore;
