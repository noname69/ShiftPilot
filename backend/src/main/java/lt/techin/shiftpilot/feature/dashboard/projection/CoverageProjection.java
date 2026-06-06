package lt.techin.shiftpilot.feature.dashboard.projection;

public interface CoverageProjection {
    Long getAssignedEmployees();
    Long getMinEmployees();
    Long getUnderstaffedShiftsCount();
}
