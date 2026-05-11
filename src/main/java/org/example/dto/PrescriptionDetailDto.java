package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescriptionDetailDto {
    @NotNull(message = "Thuốc không được để trống")
    Long medicineId;
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    Integer quantity;
}
