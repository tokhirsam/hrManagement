package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.task1.entity.Turniket;

import java.util.UUID;

public interface TurnikateRepository extends JpaRepository<Turniket, Integer> {
}
