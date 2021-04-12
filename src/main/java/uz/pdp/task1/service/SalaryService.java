package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.Salary;
import uz.pdp.task1.entity.Turniket;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.SalaryDto;
import uz.pdp.task1.payload.TurniketDto;
import uz.pdp.task1.repository.EmployeeRepository;
import uz.pdp.task1.repository.SalaryRepository;
import uz.pdp.task1.repository.TurnikateRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    EmployeeRepository employeeRepository;


    public ApiResponse add(SalaryDto dto, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();

        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not add salary", false);
        Optional<Employee> optionalEmployee = employeeRepository.findById(dto.getEmployeeId());
        if (!optionalEmployee.isPresent()) return new ApiResponse("Bunday Id li employee mavjud emas", false);

        Salary salary = new Salary();
        salary.setEmployee(optionalEmployee.get());
        salary.setAmount(dto.getAmount());
        salary.setPaid(dto.isPaid());
        salary.setMonth(dto.getMonth());
        salary.setYear(dto.getYear());
        salaryRepository.save(salary);
        return new ApiResponse("New salary added", true);
    }

    public ApiResponse edit(UUID id, SalaryDto dto, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();

        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not edit salary", false);
        Optional<Employee> optionalEmployee = employeeRepository.findById(dto.getEmployeeId());
        if (!optionalEmployee.isPresent()) return new ApiResponse("Bunday Id li employee mavjud emas", false);
        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        if (!optionalSalary.isPresent()) return new ApiResponse("Bunday Idli salary databaseda mavjud emas", false);


        Salary salary = optionalSalary.get();
        salary.setEmployee(optionalEmployee.get());
        salary.setAmount(dto.getAmount());
        salary.setPaid(dto.isPaid());
        salary.setMonth(dto.getMonth());
        salary.setYear(dto.getYear());
        salaryRepository.save(salary);
        return new ApiResponse("Salary edited", true);
    }
    public ApiResponse delete(UUID id, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        String position = employeeRepository.findByEmail(email).get().getPosition();

        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not delete salary", false);
        Optional<Salary> optionalSalary = salaryRepository.findById(id);
        if (!optionalSalary.isPresent()) return new ApiResponse("Bunday Idli salary databaseda mavjud emas", false);
        salaryRepository.deleteById(id);
        return new ApiResponse("Salary deleted", true);
    }
    public ApiResponse getSalaryByEmployee(UUID employeeId,HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();

        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not check salary info", false);
        List<Salary> salaryList = new ArrayList<>();
        List<Salary> salaryRepositoryAll = salaryRepository.findAll();
        for (Salary salary : salaryRepositoryAll) {
            if (salary.getEmployee().getId().equals(employeeId)) {
                salaryList.add(salary);
            }
        }
        return new ApiResponse("Ushbu hodimning maaoshi tarihi", true, salaryList);
    }
    public ApiResponse getSalaryByMonth(String month,Integer year, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);
        String position = employeeRepository.findByEmail(email).get().getPosition();

        if ((role.equals("ROLE_STAFF")) || ((role.equals("ROLE_MANAGER") && !position.equals("HR_MANAGER"))))
            return new ApiResponse("You can not check salary info", false);
        List<Salary> salaryList = new ArrayList<>();
        List<Salary> salaryRepositoryAll = salaryRepository.findAll();
        for (Salary salary : salaryRepositoryAll) {
            if (salary.getMonth().equals(month) && salary.getYear().equals(year)) {
                salaryList.add(salary);
            }
        }
        return new ApiResponse(year+" yil, "+month+" oyi uchun hodimlar maaoshi tarihi", true, salaryList);
    }


}
