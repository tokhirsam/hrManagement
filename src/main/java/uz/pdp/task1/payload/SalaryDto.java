package uz.pdp.task1.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class SalaryDto {
    private UUID employeeId;

    private double amount;

    private String month;

    private boolean isPaid;
    private Integer year;
}
