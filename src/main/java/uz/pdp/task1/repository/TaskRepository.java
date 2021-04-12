package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.task1.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findAllByTaskTakerEmail(String taskTaker_email);

    @Query(value = "select t.id from task t join employee e on t.task_taker_id = e.id  where t.status='STATUS_COMPLETED' and e.id=:employeeId",nativeQuery = true)
    List<UUID> completedTasks(UUID employeeId);

    @Query(value = "select count(id) from task where task_taker_id =:employeeId",nativeQuery = true)
    Integer allTaskOfEmployee(UUID employeeId);

    @Query(value = "select count(id) from task where task_taker_id =:employeeId and deadline>completed_date",nativeQuery = true)
    Integer allTaskOfEmployeeCompletedOnTime(UUID employeeId);



}
