package org.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalRecordsDto {
    private Long patientId;
    private Long appointmentId;
    private String description;
    private LocalDate date;
}
