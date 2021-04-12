package uz.pdp.task1.payload;

import lombok.Data;

@Data
public class TurniketHistoryDto {
    Integer turniketId;
    boolean goingIn;
    String employeeUsername;
}
