package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.LoginDto;
import uz.pdp.task1.payload.RegisterDto;
import uz.pdp.task1.service.AuthService;
import uz.pdp.task1.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService service;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = service.registerDirector(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse response = service.verifyEmail(emailCode, email);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto dto) {
        ApiResponse apiResponse = service.login(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
