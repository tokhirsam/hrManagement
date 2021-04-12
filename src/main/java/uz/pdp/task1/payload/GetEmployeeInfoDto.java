package uz.pdp.task1.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Data
public class GetEmployeeInfoDto {
    private UUID employeeID;
    private Timestamp from;
    private Timestamp to;
}
