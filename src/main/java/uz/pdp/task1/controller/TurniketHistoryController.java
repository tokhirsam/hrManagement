package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.TurniketDto;
import uz.pdp.task1.payload.TurniketHistoryDto;
import uz.pdp.task1.service.TurniketHistoryService;
import uz.pdp.task1.service.TurniketService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth/turniketHistory")
public class TurniketHistoryController {
    @Autowired
    TurniketHistoryService service;

    @PostMapping
    public HttpEntity<?> add(@RequestBody TurniketHistoryDto dto) {
        ApiResponse apiResponse = service.add(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
}
