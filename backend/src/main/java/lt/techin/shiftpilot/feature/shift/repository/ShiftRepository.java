package lt.techin.shiftpilot.feature.shift.repository;

import lt.techin.shiftpilot.feature.shift.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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


}
