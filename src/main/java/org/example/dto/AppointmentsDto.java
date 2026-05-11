package org.example.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppointmentsDto {
    private Long id;
    @NotNull(message = "ID bệnh nhân không được bỏ trống")
    private Long patientId;
    @NotNull(message = "ID bác sĩ không được bỏ trống")
    private Long doctorId;
    @NotNull(message = "Ngày bắt đầu không được bỏ trống")
    @Future(message = "Ngày bắt đầu phải là trong tương lai")
    private LocalDateTime startDate;
    @NotNull(message = "Ngày kết thúc không được bỏ trống")
    @Future(message = "Ngày kết thúc phải là trong tương lai")
    private LocalDateTime endDate;
    private String status;
}
