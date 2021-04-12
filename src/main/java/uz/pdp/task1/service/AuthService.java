package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.Role;
import uz.pdp.task1.entity.enums.RoleName;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.LoginDto;
import uz.pdp.task1.payload.RegisterDto;
import uz.pdp.task1.repository.EmployeeRepository;
import uz.pdp.task1.repository.RoleRepository;

import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerDirector(RegisterDto dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setEnabled(true);

        employee.setPassword(passwordEncoder.encode("12345"));
        employee.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
        employeeRepository.save(employee);
        return new ApiResponse("Director registered successfully", true);

    }



    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("khusansam@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Verify your email and get your password and username");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode +
                    "&email=" + sendingEmail + "'>Tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }


    }
    public Boolean sendEmailAboutTask(String sendingEmail, String text, String subject) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("khusansam@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setEnabled(true);
            employee.setEmailCode(null);
            String password = generateRandomPassword(8);
            employee.setPassword(passwordEncoder.encode(password));
            employeeRepository.save(employee);
            return new ApiResponse("Account verified.\n" +
                    "Your username => "+email+"\n"+
                    "Your password => "+password+"\n" +
                    "You can change your password once you logged in ", true);
        }
        return new ApiResponse("Account already verified", false);
    }

    public ApiResponse login(LoginDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (dto.getUsername(), dto.getPassword()));
            Employee employee = (Employee) authentication.getPrincipal();
            String token = jwtProvider.generateToken(dto.getUsername(), employee.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Parol yoki username xato", false);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> optionalUser = employeeRepository.findByEmail(username);
        if (optionalUser.isPresent()) return optionalUser.get();
        throw new UsernameNotFoundException(username + "topilmadi");
    }
    public static String generateRandomPassword(int len) {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
