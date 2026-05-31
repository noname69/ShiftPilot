import { create } from "zustand";
import { devtools } from "zustand/middleware";
import api from "../api/api";
import toast from "react-hot-toast";

const useLeaveStore = create(

  devtools((set) => ({

    isLoading: false,

    sendLeaveRequest: async (formData) => {
      try {
        set({ isLoading: true });
        await api.post(`/users/me/leave-requests`, formData);
        toast.success("Leave request was send successfully")
      } catch (error) {
        console.log(error);
        toast.error(error?.response?.data?.fieldError?.[0]?.message || "Something went wrong");
      } finally {
        set({ isLoading: false });
      }
    },

  })),
);

export default useLeaveStore;
