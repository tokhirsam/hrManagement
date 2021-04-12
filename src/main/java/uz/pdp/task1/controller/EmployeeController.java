package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.PasswordChangeByStaffDto;
import uz.pdp.task1.payload.RegisterDto;
import uz.pdp.task1.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/employee")
public class EmployeeController {
    @Autowired
    EmployeeService service;

    @PostMapping
    public HttpEntity<?> addEmployee(@RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.addEmployee(registerDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
    @PutMapping("/{username}")
    public HttpEntity<?> editEmployeeByDirectorAndHr
            (@RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest, @PathVariable String username) {
        ApiResponse apiResponse = service.editByDirectorAndHr(username,registerDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }
    @PutMapping("/byStaff")
    public HttpEntity<?> editEmployeeByStaff
            (@RequestBody PasswordChangeByStaffDto dto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.editByOddiyHodim(dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> delete(@PathVariable String username, HttpServletRequest httpServletRequest){
        ApiResponse response = service.delete(username, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest){
        ApiResponse response = service.getAll( httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }

    @GetMapping("/getEmployeeInfo")
    public ResponseEntity<?> getEmployeeInfo(@RequestParam UUID employee_id, @RequestParam Timestamp from,
                                             @RequestParam Timestamp to,HttpServletRequest httpServletRequest){
        ApiResponse response = service.getOneEmployeeInfo( employee_id, from, to,httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }

}
