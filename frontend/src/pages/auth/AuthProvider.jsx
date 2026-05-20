import { useEffect } from "react";
import useUsersStore from "../../store/userStore";

const AuthProvider = ({ children }) => {
  const { fetchCurrentUser, authMeIsLoading } = useUsersStore((state) => state);

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
