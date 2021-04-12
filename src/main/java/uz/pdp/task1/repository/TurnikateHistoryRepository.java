package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.task1.entity.Turniket;
import uz.pdp.task1.entity.TurniketHistory;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TurnikateHistoryRepository extends JpaRepository<TurniketHistory, UUID> {

    @Query(value = "select t.id from turniket_history t join employee e on t.employee_id = e.id \n" +
            "where e.id =:employeeId and \n" +
            "t.time between :from and :to",nativeQuery = true)
    List<UUID> kirdiChiqdi(UUID employeeId, Timestamp from, Timestamp to);
}
