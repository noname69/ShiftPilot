import useAuthStore from "../../store/authStore";
import ManagerDashboard from "./ManagerDashboard";
import EmployeeDashboard from "./EmployeeDashboard";

const Dashboard = () => {
  const role = useAuthStore((s) => s.user.role);

  if (role === "MANAGER" || role === "ADMIN") {
    return <ManagerDashboard />;
  }

  return <EmployeeDashboard />;
};

export default Dashboard;
