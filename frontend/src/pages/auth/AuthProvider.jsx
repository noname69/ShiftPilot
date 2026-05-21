import { useEffect } from "react";
import useAuthStore from "../../store/authStore";

const AuthProvider = ({ children }) => {
  const { fetchCurrentUser, authMeIsLoading } = useAuthStore((state) => state);

  useEffect(() => {
    fetchCurrentUser();
  }, [fetchCurrentUser]);

  if (authMeIsLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <h1>IS LOADING...</h1>
      </div>
    );
  }

  return children;
};

export default AuthProvider;
