package uz.pdp.task1.payload;

import lombok.Data;
import uz.pdp.task1.entity.Task;
import uz.pdp.task1.entity.TurniketHistory;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Data
public class GetEmployeeInfo {
    String fistName;
    String lastName;
    List<Task> completedTasks;
    List<TurniketHistory> keldiKetdis;

}
