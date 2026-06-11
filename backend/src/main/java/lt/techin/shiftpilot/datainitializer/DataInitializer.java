package lt.techin.shiftpilot.datainitializer;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.dto.CreateLeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.service.LeaveRequestService;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shift.repository.ShiftRepository;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.service.ShiftAssignmentService;
import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import lt.techin.shiftpilot.feature.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DataInitializer {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;
    private final ShiftAssignmentService shiftAssignmentService;
    private final LeaveRequestService leaveRequestService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {

//            CreateUserRequest createAdmin = new CreateUserRequest("Admin", "Admin", "admin@example.com", "admin", "Password123", UserRole.ADMIN);
//            userService.createUser(createAdmin);
//
//            CreateUserRequest createManager1 = new CreateUserRequest("Manager1", "Manager1", "manager1@example.com", "manager1", "Password123", UserRole.MANAGER);
//            userService.createUser(createManager1);
//
//            CreateUserRequest createManager2 = new CreateUserRequest("Manager2", "Manager2", "manager2@example.com", "manager2", "Password123", UserRole.MANAGER);
//            userService.createUser(createManager2);
//
//            for (int i = 1; i <= 20; i++) {
//
//                CreateUserRequest createWorker = new CreateUserRequest(
//                        "Worker" + i,
//                        "Worker" + i,
//                        "worker" + i + "@example.com",
//                        "worker" + i,
//                        "Password123",
//                        UserRole.USER
//                );
//
//                userService.createUser(createWorker);
//            }
//
//            User worker11 = userRepository.findByUsername("worker11")
//                    .orElseThrow(() -> new UserNotFoundException("worker11"));
//
//            User worker12 = userRepository.findByUsername("worker12")
//                    .orElseThrow(() -> new UserNotFoundException("worker12"));
//
//            User worker13 = userRepository.findByUsername("worker13")
//                    .orElseThrow(() -> new UserNotFoundException("worker13"));
//
//            User worker14 = userRepository.findByUsername("worker14")
//                    .orElseThrow(() -> new UserNotFoundException("worker14"));
//
//            User worker15 = userRepository.findByUsername("worker15")
//                    .orElseThrow(() -> new UserNotFoundException("worker15"));
//
//            User worker16 = userRepository.findByUsername("worker16")
//                    .orElseThrow(() -> new UserNotFoundException("worker16"));
//
//            User worker17 = userRepository.findByUsername("worker17")
//                    .orElseThrow(() -> new UserNotFoundException("worker17"));
//
//            User worker18 = userRepository.findByUsername("worker18")
//                    .orElseThrow(() -> new UserNotFoundException("worker18"));
//
//            User worker19 = userRepository.findByUsername("worker19")
//                    .orElseThrow(() -> new UserNotFoundException("worker19"));
//
//            User worker20 = userRepository.findByUsername("worker20")
//                    .orElseThrow(() -> new UserNotFoundException("worker20"));
//
//            User admin = userRepository.findByUsername("admin")
//                    .orElseThrow(() -> new UserNotFoundException("admin"));
//
//            User manager1 = userRepository.findByUsername("manager1")
//                    .orElseThrow(() -> new UserNotFoundException("manager1"));
//
//            User manager2 = userRepository.findByUsername("manager2")
//                    .orElseThrow(() -> new UserNotFoundException("manager2"));
//
//
//            User worker1 = userRepository.findByUsername("worker1")
//                    .orElseThrow(() -> new UserNotFoundException("worker1"));
//
//            User worker2 = userRepository.findByUsername("worker2")
//                    .orElseThrow(() -> new UserNotFoundException("worker2"));
//
//            User worker3 = userRepository.findByUsername("worker3")
//                    .orElseThrow(() -> new UserNotFoundException("worker3"));
//
//            User worker4 = userRepository.findByUsername("worker4")
//                    .orElseThrow(() -> new UserNotFoundException("worker4"));
//
//            User worker5 = userRepository.findByUsername("worker5")
//                    .orElseThrow(() -> new UserNotFoundException("worker5"));
//
//            User worker6 = userRepository.findByUsername("worker6")
//                    .orElseThrow(() -> new UserNotFoundException("worker6"));
//
//            User worker7 = userRepository.findByUsername("worker7")
//                    .orElseThrow(() -> new UserNotFoundException("worker7"));
//
//            User worker8 = userRepository.findByUsername("worker8")
//                    .orElseThrow(() -> new UserNotFoundException("worker8"));
//
//            User worker9 = userRepository.findByUsername("worker9")
//                    .orElseThrow(() -> new UserNotFoundException("worker9"));
//
//            User worker10 = userRepository.findByUsername("worker10")
//                    .orElseThrow(() -> new UserNotFoundException("worker10"));
//
//            Shift shift1 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().minusDays(1))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager2)
//                    .minEmployees(5)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift1 = shiftRepository.save(shift1);
//
//            Shift shift2 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().minusDays(2))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager1)
//                    .minEmployees(6)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift2 = shiftRepository.save(shift2);
//
//            Shift shift3 = Shift.builder()
//                    .title("Night shift")
//                    .description("Night monitoring and operations")
//                    .shiftDate(LocalDate.now().minusDays(3))
//                    .startTime(LocalTime.of(0, 0))
//                    .endTime(LocalTime.of(6, 0))
//                    .createdBy(manager2)
//                    .minEmployees(7)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift3 = shiftRepository.save(shift3);
//
//            Shift shift4 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().minusDays(4))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager2)
//                    .minEmployees(8)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift4 = shiftRepository.save(shift4);
//
//            Shift shift5 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().minusDays(5))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager1)
//                    .minEmployees(9)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift5 = shiftRepository.save(shift5);
//
//            Shift shift6 = Shift.builder()
//                    .title("Night shift")
//                    .description("Night monitoring and operations")
//                    .shiftDate(LocalDate.now().plusDays(1))
//                    .startTime(LocalTime.of(0, 0))
//                    .endTime(LocalTime.of(6, 0))
//                    .createdBy(manager2)
//                    .minEmployees(5)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift6 = shiftRepository.save(shift6);
//
//            Shift shift7 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().plusDays(2))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager1)
//                    .minEmployees(6)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift7 = shiftRepository.save(shift7);
//
//            Shift shift8 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().plusDays(3))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager2)
//                    .minEmployees(7)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift8 = shiftRepository.save(shift8);
//
//            Shift shift9 = Shift.builder()
//                    .title("Night shift")
//                    .description("Night monitoring and operations")
//                    .shiftDate(LocalDate.now().plusDays(4))
//                    .startTime(LocalTime.of(0, 0))
//                    .endTime(LocalTime.of(6, 0))
//                    .createdBy(manager1)
//                    .minEmployees(8)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift9 = shiftRepository.save(shift9);
//
//            Shift shift10 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().plusDays(5))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager2)
//                    .minEmployees(9)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift10 = shiftRepository.save(shift10);
//
//            Shift shift11 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().plusDays(6))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager1)
//                    .minEmployees(10)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift11 = shiftRepository.save(shift11);
//
//            Shift shift12 = Shift.builder()
//                    .title("Night shift")
//                    .description("Night monitoring and operations")
//                    .shiftDate(LocalDate.now().plusDays(7))
//                    .startTime(LocalTime.of(0, 0))
//                    .endTime(LocalTime.of(6, 0))
//                    .createdBy(manager1)
//                    .minEmployees(5)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift12 = shiftRepository.save(shift12);
//
//            Shift shift13 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().plusDays(8))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager2)
//                    .minEmployees(6)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift13 = shiftRepository.save(shift13);
//
//            Shift shift14 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().plusDays(9))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager1)
//                    .minEmployees(7)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift14 = shiftRepository.save(shift14);
//
//            Shift shift15 = Shift.builder()
//                    .title("Night shift")
//                    .description("Night monitoring and operations")
//                    .shiftDate(LocalDate.now().plusDays(10))
//                    .startTime(LocalTime.of(0, 0))
//                    .endTime(LocalTime.of(6, 0))
//                    .createdBy(manager2)
//                    .minEmployees(8)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift15 = shiftRepository.save(shift15);
//
//            Shift shift16 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().plusDays(1))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager1)
//                    .minEmployees(9)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift16 = shiftRepository.save(shift16);
//
//            Shift shift17 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().plusDays(2))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager2)
//                    .minEmployees(10)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift17 = shiftRepository.save(shift17);
//
//            Shift shift18 = Shift.builder()
//                    .title("Night shift")
//                    .description("Night monitoring and operations")
//                    .shiftDate(LocalDate.now().plusDays(3))
//                    .startTime(LocalTime.of(0, 0))
//                    .endTime(LocalTime.of(6, 0))
//                    .createdBy(manager1)
//                    .minEmployees(5)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift18 = shiftRepository.save(shift18);
//
//            Shift shift19 = Shift.builder()
//                    .title("Morning shift")
//                    .description("Opening, preparation, and early operations support")
//                    .shiftDate(LocalDate.now().plusDays(4))
//                    .startTime(LocalTime.of(6, 0))
//                    .endTime(LocalTime.of(14, 0))
//                    .createdBy(manager2)
//                    .minEmployees(6)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift19 = shiftRepository.save(shift19);
//
//            Shift shift20 = Shift.builder()
//                    .title("Evening shift")
//                    .description("Midday operations and customer support")
//                    .shiftDate(LocalDate.now().plusDays(5))
//                    .startTime(LocalTime.of(14, 0))
//                    .endTime(LocalTime.of(22, 0))
//                    .createdBy(manager1)
//                    .minEmployees(7)
//                    .status(ShiftStatus.OPEN)
//                    .build();
//            Shift savedShift20 = shiftRepository.save(shift20);
//
//            List<Long> userIds1 = List.of(
//                    worker1.getId(), worker4.getId(), worker7.getId(), worker12.getId(), worker18.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds1), savedShift1.getId());
//
//            List<Long> userIds2 = List.of(
//                    worker2.getId(), worker5.getId(), worker9.getId(), worker13.getId(), worker19.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds2), savedShift2.getId());
//
//            List<Long> userIds3 = List.of(
//                    worker3.getId(), worker6.getId(), worker10.getId(), worker14.getId(), worker20.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds3), savedShift3.getId());
//
//            List<Long> userIds4 = List.of(
//                    worker1.getId(), worker5.getId(), worker8.getId(), worker11.getId(), worker15.getId(), worker19.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds4), savedShift4.getId());
//
//            List<Long> userIds5 = List.of(
//                    worker2.getId(), worker6.getId(), worker9.getId(), worker12.getId(), worker16.getId(), worker20.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds5), savedShift5.getId());
//
//            List<Long> userIds6 = List.of(
//                    worker3.getId(), worker7.getId(), worker10.getId(), worker13.getId(), worker17.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds6), savedShift6.getId());
//
//            List<Long> userIds7 = List.of(
//                    worker4.getId(), worker8.getId(), worker11.getId(), worker14.getId(), worker18.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds7), savedShift7.getId());
//
//            List<Long> userIds8 = List.of(
//                    worker1.getId(), worker6.getId(), worker10.getId(), worker15.getId(), worker17.getId(), worker20.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds8), savedShift8.getId());
//
//            List<Long> userIds9 = List.of(
//                    worker2.getId(), worker7.getId(), worker11.getId(), worker16.getId(), worker18.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds9), savedShift9.getId());
//
//            List<Long> userIds10 = List.of(
//                    worker3.getId(), worker8.getId(), worker12.getId(), worker14.getId(), worker19.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds10), savedShift10.getId());
//
//            List<Long> userIds11 = List.of(
//                    worker4.getId(), worker9.getId(), worker13.getId(), worker15.getId(), worker20.getId(), worker1.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds11), savedShift11.getId());
//
//            List<Long> userIds12 = List.of(
//                    worker5.getId(), worker10.getId(), worker14.getId(), worker16.getId(), worker17.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds12), savedShift12.getId());
//
//            List<Long> userIds13 = List.of(
//                    worker6.getId(), worker11.getId(), worker15.getId(), worker18.getId(), worker2.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds13), savedShift13.getId());
//
//            List<Long> userIds14 = List.of(
//                    worker7.getId(), worker12.getId(), worker16.getId(), worker19.getId(), worker3.getId(), worker5.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds14), savedShift14.getId());
//
//            List<Long> userIds15 = List.of(
//                    worker8.getId(), worker13.getId(), worker17.getId(), worker20.getId(), worker4.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds15), savedShift15.getId());
//
//            List<Long> userIds16 = List.of(
//                    worker9.getId(), worker14.getId(), worker18.getId(), worker1.getId(), worker6.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds16), savedShift16.getId());
//
//            List<Long> userIds17 = List.of(
//                    worker10.getId(), worker15.getId(), worker19.getId(), worker2.getId(), worker7.getId(), worker11.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds17), savedShift17.getId());
//
//            List<Long> userIds18 = List.of(
//                    worker12.getId(), worker16.getId(), worker20.getId(), worker3.getId(), worker8.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds18), savedShift18.getId());
//
//            List<Long> userIds19 = List.of(
//                    worker13.getId(), worker17.getId(), worker1.getId(), worker4.getId(), worker9.getId(), worker18.getId());
//            shiftAssignmentService.assignShift("manager1", new ShiftAssignRequest(userIds19), savedShift19.getId());
//
//            List<Long> userIds20 = List.of(
//                    worker14.getId(), worker19.getId(), worker2.getId(), worker5.getId(), worker10.getId(), worker15.getId(), worker20.getId());
//            shiftAssignmentService.assignShift("manager2", new ShiftAssignRequest(userIds20), savedShift20.getId());
//
//
//            CreateLeaveRequest leaveRequest1 = new CreateLeaveRequest("Sick leave", RequestType.ILL, LocalDate.now(), LocalDate.now().plusDays(3), manager1.getId());
//            leaveRequestService.createLeaveRequest(worker20.getId(), leaveRequest1);
//
//            CreateLeaveRequest leaveRequest2 = new CreateLeaveRequest("Personal reason", RequestType.ABSENCE, LocalDate.now(), LocalDate.now(), manager1.getId());
//            leaveRequestService.createLeaveRequest(worker16.getId(), leaveRequest2);
//
//            CreateLeaveRequest leaveRequest3 = new CreateLeaveRequest("Going for vacation", RequestType.VACATION, LocalDate.now().plusDays(7), LocalDate.now().plusDays(14), manager2.getId());
//            leaveRequestService.createLeaveRequest(worker17.getId(), leaveRequest3);
//
//            CreateLeaveRequest leaveRequest4 = new CreateLeaveRequest("Child got sick", RequestType.ABSENCE, LocalDate.now(), LocalDate.now().plusDays(3), manager2.getId());
//            leaveRequestService.createLeaveRequest(worker11.getId(), leaveRequest4);
//

        };
    }
}
