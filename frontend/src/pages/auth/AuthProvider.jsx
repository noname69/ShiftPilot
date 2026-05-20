import { useEffect } from "react";
import useUsersStore from "../../store/userStore";

const AuthProvider = ({ children }) => {
  const { fetchCurrentUser, isLoading } = useUsersStore((state) => state);

  useEffect(() => {
    fetchCurrentUser();
  }, [fetchCurrentUser]);

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <h1>IS LOADING...</h1>
      </div>
    );
  }

  return children;
};

export default AuthProvider;
