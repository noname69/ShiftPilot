package lt.techin.shiftpilot.feature.dashboard.service;

import lt.techin.shiftpilot.feature.dashboard.dto.EmployeeDashboardResponse;
import lt.techin.shiftpilot.feature.dashboard.dto.ManagerDashboardResponse;

import java.time.LocalDate;

public interface DashboardService {
    ManagerDashboardResponse getManagerDashboard(Long managerId, LocalDate weekStart, LocalDate weekEnd);
    EmployeeDashboardResponse getEmployeeDashboard(Long userId, LocalDate weekStart, LocalDate weekEnd);
}
