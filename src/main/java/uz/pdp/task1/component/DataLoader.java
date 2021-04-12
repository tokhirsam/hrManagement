package uz.pdp.task1.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.Role;
import uz.pdp.task1.entity.enums.RoleName;
import uz.pdp.task1.repository.EmployeeRepository;
import uz.pdp.task1.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            Role directorRole = new Role();
            directorRole.setRoleName(RoleName.ROLE_DIRECTOR);
            Role staff = new Role();
            staff.setRoleName(RoleName.ROLE_STAFF);
            Role manager = new Role();
            manager.setRoleName(RoleName.ROLE_MANAGER);
            roleRepository.save(directorRole);
            roleRepository.save(staff);
            roleRepository.save(manager);


            Employee director = new Employee();
            director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
            director.setEmail("samatov1992@gmail.com");
            director.setFirstName("John");
            director.setLastName("Smith");
            director.setPosition("boss");
            director.setPassword(passwordEncoder.encode("12345"));
            director.setEnabled(true);
            employeeRepository.save(director);




        }
    }
}
