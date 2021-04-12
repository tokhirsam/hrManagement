package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Task;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.Role;
import uz.pdp.task1.entity.enums.RoleName;
import uz.pdp.task1.entity.enums.TaskStatus;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.TaskDto;
import uz.pdp.task1.payload.TaskStatusChangeDto;
import uz.pdp.task1.repository.EmployeeRepository;
import uz.pdp.task1.repository.RoleRepository;
import uz.pdp.task1.repository.TaskRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthService authService;


    public ApiResponse add(TaskDto dto, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (role.equals("ROLE_STAFF")) return new ApiResponse("You can not add task", false);
        Optional<Employee> optionalTaskTaker = employeeRepository.findByEmail(dto.getTaskTakerEmail());
        if (!optionalTaskTaker.isPresent()) return new
                ApiResponse("Siz vazifani biriktirmoqchi bolgan hodimning email notogri", false);
        Set<Role> taskTakerRoles = optionalTaskTaker.get().getRoles();
        for (Role taskTakerRole : taskTakerRoles) {
            if (role.equals("ROLE_DIRECTOR") && taskTakerRole.equals(roleRepository.findByRoleName(RoleName.ROLE_STAFF))) {
                return new ApiResponse("Siz faqat menejerlarga vazifa biriktira olasiz", false);
            } else if (role.equals("ROLE_MANAGER") && taskTakerRole.equals(roleRepository.findByRoleName(RoleName.ROLE_MANAGER))) {
                return new ApiResponse("Siz faqat hodimlarga vazifa biriktira olasiz", false);
            } else if (role.equals("ROLE_MANAGER") && taskTakerRole.equals(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR))) {
                return new ApiResponse("Sekin, Sekin. Directorga topshiriq berish sizgamas. Siz faqat hodimlarga vazifa biriktira olasiz", false);
            }

        }
        Task task = new Task();
        task.setTaskTaker(optionalTaskTaker.get());
        task.setDeadline(dto.getDeadline());
        task.setDescription(dto.getDescription());
        task.setName(dto.getName());
        task.setTaskGiver(employeeRepository.findByEmail(email).get());
        task.setStatus(TaskStatus.STATUS_NEW);

        String emailBody = "Sizga " + employeeRepository.findByEmail(email).get().getFirstName() + " tomonidan vazifa biriktirildi." +
                "1) Vazifa nomi =>" + dto.getName() + ". 2) Vazifa izohi =>" + dto.getDescription() + ". 3) Vazifani bajarish muddati =>" + dto.getDeadline();
        String emailSubject = "Sizga vazifa biriktirildi";
        authService.sendEmailAboutTask(dto.getTaskTakerEmail(), emailBody, emailSubject);
        taskRepository.save(task);
        return new ApiResponse("Yangi task qoshildi va Vazifa biriktirilgan xodimning email manziliga xabar jo’natildi", true);
    }

    public ApiResponse getYourTask(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        List<Task> tasks = taskRepository.findAll();
        List<Task> tasksAttached = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskTaker().getEmail().equals(email)){
                tasksAttached.add(task);
                task.setStatus(TaskStatus.STATUS_PROGRESS);
                taskRepository.save(task);
            }
        }
        if (tasksAttached.size()==0) return new ApiResponse("Hozircha sizga hech qanday task biriktirilmagan", false);
        return new ApiResponse("Quyidagi tasklar sizga biriktirilgan", true, tasksAttached);

    }

    public ApiResponse editTask(UUID id, TaskStatusChangeDto dto, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return new ApiResponse("Bunday Idli task mavjud emas", false);
        if (!optionalTask.get().getTaskTaker().getEmail().equals(email))
            return new ApiResponse("Bu task sizga tegishli emas", false);
        Task task = optionalTask.get();
        String emailBody = "Siz biriktirgan vazifa bajarildi. Bajaruvchi =>" + employeeRepository.findByEmail(email).get().getFirstName() +
                "1) Vazifa nomi =>" + optionalTask.get().getName() + ". 2) Vazifa izohi =>" + optionalTask.get().getDescription() + ". 3) Vazifani bajarish muddati =>" + optionalTask.get().getDeadline();
        String emailSubject = "Siz biriktirgan vazifa bajarildi";
        if (dto.getTaskStatus().equalsIgnoreCase("completed")){
            task.setStatus(TaskStatus.STATUS_COMPLETED);
            task.setCompletedDate(Date.valueOf(LocalDate.now()));
            taskRepository.save(task);
            authService.sendEmailAboutTask(optionalTask.get().getTaskGiver().getEmail(), emailBody, emailSubject);
            return new ApiResponse("Task edited", true);
        }
        return new ApiResponse("Task not edited. You can only edit the task when it is completed", false);

    }
    public ApiResponse delete(UUID id, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (role.equals("ROLE_STAFF")) return new ApiResponse("You can not delete task", false);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) return new ApiResponse("Bunday Idli task mavjud emas", false);
        if (!optionalTask.get().getTaskGiver().getEmail().equals(email))
            return new ApiResponse("Siz bu taskni delete qilolmaysiz. Bu task boshqa tomonidan kiritilgan", false);

        taskRepository.deleteById(id);
        return new ApiResponse("Task deleted", false);

    }

    public ApiResponse getOnTimeCompletedTasks(UUID employeeId, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();

        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("Siz hodimga berilgan vazifalarni o’z vaqtida bajargani yoki o’z vaqtida bajara olmayotgani xaqida malumotlarni ko’ra olmaysiz", false);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) return new ApiResponse("Bunday Id li employee mavjud emas ", false);
        Employee employee = optionalEmployee.get();
        Integer allTaskOfEmployee = taskRepository.allTaskOfEmployee(employeeId);
        Integer allTaskOfEmployeeCompletedOnTime = taskRepository.allTaskOfEmployeeCompletedOnTime(employeeId);
        return new ApiResponse("Hodimning ismi-familiyasi =>"+employee.getFirstName()+" "+employee.getLastName()+
                "Hodimga biriktilgan barcha vazifalar soni =>"+allTaskOfEmployee+
                "Hodimga o'z vaqtida bajargan barcha vazifalar soni =>"+allTaskOfEmployeeCompletedOnTime, true);

    }

}
