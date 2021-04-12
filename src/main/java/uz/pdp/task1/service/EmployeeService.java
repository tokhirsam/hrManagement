package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.Role;
import uz.pdp.task1.entity.Task;
import uz.pdp.task1.entity.TurniketHistory;
import uz.pdp.task1.entity.enums.RoleName;
import uz.pdp.task1.payload.*;
import uz.pdp.task1.repository.EmployeeRepository;
import uz.pdp.task1.repository.RoleRepository;
import uz.pdp.task1.repository.TaskRepository;
import uz.pdp.task1.repository.TurnikateHistoryRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthService authService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TurnikateHistoryRepository turnikateHistoryRepository;

    public ApiResponse addEmployee(RegisterDto dto, HttpServletRequest httpServletRequest) {
        boolean existsByEmail = employeeRepository.existsByEmail(dto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("This email address already exists", false);
        }
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);

        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();
        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not add employee", false);

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());

        employee.setPassword(passwordEncoder.encode("12345"));

        if (role.equals("ROLE_DIRECTOR"))
            employee.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)));

        if (role.equals("ROLE_MANAGER"))
            employee.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_STAFF)));

        employee.setEmailCode(UUID.randomUUID().toString());
        employeeRepository.save(employee);
        authService.sendEmail(employee.getEmail(), employee.getEmailCode());
        return new ApiResponse("Registered successfully, Verify your email and get your password and username", true);

    }

    public ApiResponse editByDirectorAndHr(String username, RegisterDto dto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);

        String email = jwtProvider.getEmailFromToken(token);
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(username);
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Bunday usernameli employee mavjud emas", false);
        String position = employeeRepository.findByEmail(email).get().getPosition();
        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not edit employee information", false);

        Employee employee = optionalEmployee.get();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employeeRepository.save(employee);
        return new ApiResponse("Employee information edited", true);

    }

    public ApiResponse editByOddiyHodim(PasswordChangeByStaffDto dto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);

        String email = jwtProvider.getEmailFromToken(token);
        if (!role.equals("ROLE_STAFF"))
            return new ApiResponse("Manager va Directorlar bu yol orqali hodim passwordini ozgartira olmaydi," +
                    " http://localhost:8080/api/auth/employee/{username} ga PUT orqali murojat qiling", false);

        Employee employee = employeeRepository.findByEmail(email).get();
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employeeRepository.save(employee);
        return new ApiResponse("Your password changed successfully", true);

    }

    public ApiResponse delete(String username, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(username);
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Bunday usernameli employee mavjud emas", false);
        String position = employeeRepository.findByEmail(email).get().getPosition();
        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not delete employee information", false);
        employeeRepository.deleteById(optionalEmployee.get().getId());
        return new ApiResponse("Employee deleted", true);
    }

    public ApiResponse getAll(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);

        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();
        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can get list of employees", false);
        return new ApiResponse("Barcha hodimlar royhati", true, employeeRepository.findAll());
    }

    public ApiResponse getOneEmployeeInfo(UUID employeeId, Timestamp from, Timestamp to, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);

        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();
        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can get  employee info", false);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) return new ApiResponse("Bunday ID li employee mavjud emas", false);

        GetEmployeeInfo getEmployeeInfo = new GetEmployeeInfo();
        getEmployeeInfo.setLastName(optionalEmployee.get().getLastName());
        getEmployeeInfo.setFistName(optionalEmployee.get().getFirstName());

        List<UUID> completedTasks = taskRepository.completedTasks(employeeId);
        List<Task> completedTasksList = new ArrayList<>();
        for (UUID task : completedTasks) {
            Task completed = taskRepository.findById(task).get();
            completedTasksList.add(completed);

        }
        List<UUID> kirdiChiqdilar = turnikateHistoryRepository.kirdiChiqdi(employeeId, from, to);
        List<TurniketHistory> allKirdiChiqdi = new ArrayList<>();
        for (UUID kirdiChiqdi : kirdiChiqdilar) {
            TurniketHistory kirchiq = turnikateHistoryRepository.findById(kirdiChiqdi).get();
            allKirdiChiqdi.add(kirchiq);

        }

        getEmployeeInfo.setCompletedTasks(completedTasksList);
        getEmployeeInfo.setKeldiKetdis(allKirdiChiqdi);
        return new ApiResponse("Info employee", true, getEmployeeInfo);

    }


}
