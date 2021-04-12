package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.task1.entity.Salary;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary, UUID> {


}
