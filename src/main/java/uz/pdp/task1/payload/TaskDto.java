package uz.pdp.task1.payload;

import lombok.Data;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.enums.TaskStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.sql.Date;
import java.sql.Timestamp;
@Data
public class TaskDto {

    private String name;
    private String description;
    private Date deadline;
    private String taskTakerEmail;//qabul qiluvchi
}
