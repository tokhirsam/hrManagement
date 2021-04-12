package uz.pdp.task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.task1.controller.template.AbsEntity;


import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Salary extends AbsEntity {

    @ManyToOne
    private Employee employee;

    @Column(nullable = false)
    private double amount;

    private String month;

    private Integer year;

    private boolean isPaid = false; //oy uchun to'langanlik holati
}
