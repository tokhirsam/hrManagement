package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Task;
import uz.pdp.task1.entity.Turniket;
import uz.pdp.task1.entity.TurniketHistory;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.TurniketDto;
import uz.pdp.task1.repository.TurnikateRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class TurniketService {
    @Autowired
    TurnikateRepository turnikateRepository;
    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse add(TurniketDto dto, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (!role.equals("ROLE_DIRECTOR")) return new ApiResponse("You can not add new turniket", false);
        Turniket turniket = new Turniket();
        turniket.setLocation(dto.getLocation());
        turnikateRepository.save(turniket);
        return new ApiResponse("New turniket added", true);
    }

    public ApiResponse edit(Integer id, TurniketDto dto, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (!role.equals("ROLE_DIRECTOR")) return new ApiResponse("You can not edit turniket", false);
        Optional<Turniket> optionalTurniket = turnikateRepository.findById(id);
        if (!optionalTurniket.isPresent()) return new ApiResponse("Bunday IDli turniket mavjud emas", false);
        Turniket turniket  = optionalTurniket.get();
        turniket.setLocation(dto.getLocation());
        turnikateRepository.save(turniket);
        return new ApiResponse("Turniket edited", true);
    }
    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (!role.equals("ROLE_DIRECTOR")) return new ApiResponse("You can not delete turniket", false);
        Optional<Turniket> optionalTurniket = turnikateRepository.findById(id);
        if (!optionalTurniket.isPresent()) return new ApiResponse("Bunday IDli turniket mavjud emas", false);
        turnikateRepository.deleteById(id);
        return new ApiResponse("Turniket deleted", true);
    }
    public ApiResponse getAll(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String role = jwtProvider.getRoleFromToken(token);
        String email = jwtProvider.getEmailFromToken(token);

        if (!role.equals("ROLE_DIRECTOR")) return new ApiResponse("You can not check turniket list", false);
        return new ApiResponse("Barcha turniketlar royhati", true,turnikateRepository.findAll());
    }


}
