package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.*;
import uz.pdp.task1.service.EmployeeService;
import uz.pdp.task1.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/task")
public class TaskController {
    @Autowired
    TaskService service;

    @PostMapping
    public HttpEntity<?> addEmployee(@RequestBody TaskDto dto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.add(dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
    @PutMapping("/{id}")
    public HttpEntity<?> editByEmployeeToChangeTaskStatus
            (@RequestBody TaskStatusChangeDto dto, HttpServletRequest httpServletRequest, @PathVariable UUID id) {
        ApiResponse apiResponse = service.editTask(id,dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, HttpServletRequest httpServletRequest){
        ApiResponse response = service.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }
    @GetMapping
    public ResponseEntity<?>getYourTasks(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getYourTask(httpServletRequest));
    }
    @GetMapping("/getOnTimeCompletedTasks/{employeeId}")
    public ResponseEntity<?> getOnTimeCompletedTasks(@PathVariable UUID employeeId, HttpServletRequest httpServletRequest){
        ApiResponse response = service.getOnTimeCompletedTasks(employeeId, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }

}
