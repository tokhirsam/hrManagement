package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.payload.GetEmployeeInfo;


import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String email);
    Optional <Employee> findByEmailAndEmailCode(String email, String emailCode);
    Optional <Employee> findByEmail(String email);
    Optional <Employee> deleteByEmail(String email);


}
