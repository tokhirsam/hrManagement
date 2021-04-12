package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.TaskDto;
import uz.pdp.task1.payload.TaskStatusChangeDto;
import uz.pdp.task1.payload.TurniketDto;
import uz.pdp.task1.service.TaskService;
import uz.pdp.task1.service.TurniketService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/turniket")
public class TurniketController {
    @Autowired
    TurniketService service;

    @PostMapping
    public HttpEntity<?> add(@RequestBody TurniketDto dto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.add(dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
    @PutMapping("/{id}")
    public HttpEntity<?> edit
            (@RequestBody TurniketDto dto, HttpServletRequest httpServletRequest, @PathVariable Integer id) {
        ApiResponse apiResponse = service.edit(id,dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        ApiResponse response = service.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }
    @GetMapping
    public ResponseEntity<?>getAll(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getAll(httpServletRequest));
    }
}
