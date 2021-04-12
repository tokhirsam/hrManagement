package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.SalaryDto;
import uz.pdp.task1.payload.TaskDto;
import uz.pdp.task1.payload.TaskStatusChangeDto;
import uz.pdp.task1.service.SalaryService;
import uz.pdp.task1.service.TaskService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/salary")
public class SalaryController {
    @Autowired
    SalaryService service;

    @PostMapping
    public HttpEntity<?> add(@RequestBody SalaryDto dto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.add(dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
    @PutMapping("/{id}")
    public HttpEntity<?> editByEmployeeToChangeTaskStatus
            (@RequestBody SalaryDto dto, HttpServletRequest httpServletRequest, @PathVariable UUID id) {
        ApiResponse apiResponse = service.edit(id,dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, HttpServletRequest httpServletRequest){
        ApiResponse response = service.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<?>getSalaryByEmployee(UUID employeeId,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getSalaryByEmployee(employeeId,httpServletRequest));
    }
    @GetMapping("/getSalaryByMonth}")
    public ResponseEntity<?> getSalaryByMonth(String month, Integer year, HttpServletRequest httpServletRequest){
        ApiResponse response = service.getSalaryByMonth(month,year, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }
}
