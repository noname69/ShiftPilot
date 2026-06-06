package lt.techin.shiftpilot.feature.shift.repository;

import lt.techin.shiftpilot.feature.dashboard.projection.CoverageProjection;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> {

    @Query("""
    select s
    from Shift s
    where s.shiftDate = :shiftDate
      and s.startTime < :endTime
      and s.endTime > :startTime
    """)
    List<Shift> findOverlappingShifts(
            @Param("shiftDate") LocalDate shiftDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query(value = """
        SELECT
            COALESCE(SUM(x.assigned_count), 0) AS assignedEmployees,
            COALESCE(SUM(x.min_employees), 0) AS minEmployees,
            COALESCE(SUM(
                CASE
                    WHEN x.assigned_count < x.min_employees THEN 1
                    ELSE 0
                END
            ), 0) AS understaffedShiftsCount
        FROM (
            SELECT
                s.id,
                s.min_employees,
                COUNT(sa.id) AS assigned_count
            FROM shifts s
            LEFT JOIN shift_assignments sa
                ON sa.shift_id = s.id
               AND sa.status = 'ASSIGNED'
            WHERE s.created_by_user_id = :managerId
              AND s.shift_date BETWEEN :weekStart AND :weekEnd
            GROUP BY s.id, s.min_employees
        ) x
        """, nativeQuery = true)
    CoverageProjection getCoverageForManagerWeek(
            @Param("managerId") Long managerId,
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd
    );


}
