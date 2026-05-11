package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescriptionDto {
    @NotNull(message = "Medical record không được để trống")
    private Long medicalRecordId;
    @NotBlank(message = "Mô tả không được để trống")
    private String description;
    private String status;
    @NotEmpty(message = "Đơn thuốc phải có ít nhất 1 thuốc")
    private List<PrescriptionDetailDto> medicines;
}
