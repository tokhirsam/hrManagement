package uz.pdp.task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.task1.controller.template.AbsEntity;
import uz.pdp.task1.entity.Employee;
import uz.pdp.task1.entity.enums.TaskStatus;


import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Task extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private Date deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(optional = false)
    private Employee taskTaker;//qabul qiluvchi

    private Date completedDate;

    @ManyToOne(optional = false)
    private Employee taskGiver;//vazifa beruvchi
}
