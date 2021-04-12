package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.Turniket;
import uz.pdp.task1.entity.TurniketHistory;
import uz.pdp.task1.entity.enums.TurniketType;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.TurniketHistoryDto;
import uz.pdp.task1.repository.EmployeeRepository;
import uz.pdp.task1.repository.TurnikateHistoryRepository;
import uz.pdp.task1.repository.TurnikateRepository;

import java.util.Optional;

@Service
public class TurniketHistoryService {
    @Autowired
    TurnikateHistoryRepository turnikateHistoryRepository;
    @Autowired
    TurnikateRepository turnikateRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public ApiResponse add(TurniketHistoryDto dto){
        TurniketHistory turniketHistory = new TurniketHistory();
        Optional<Turniket> optionalTurniket = turnikateRepository.findById(dto.getTurniketId());
        if (!optionalTurniket.isPresent()) return new ApiResponse("Bunday ID li turniket mavjud emas", false);
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(dto.getEmployeeUsername());
        if (!optionalEmployee.isPresent()) return new ApiResponse("Bunday ID li hodim mavjud emas", false);

        turniketHistory.setTurniket(optionalTurniket.get());
        turniketHistory.setEmployee(optionalEmployee.get());
        if (dto.isGoingIn()){
            turniketHistory.setType(TurniketType.STATUS_IN);
        }else {
            turniketHistory.setType(TurniketType.STATUS_OUT);
        }
        turnikateHistoryRepository.save(turniketHistory);
        return new ApiResponse("Turnikatedan kirish-chiqish databasega saqlandi", true);
    }
}
