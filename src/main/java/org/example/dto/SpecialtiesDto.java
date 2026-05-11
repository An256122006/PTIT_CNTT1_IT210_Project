package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpecialtiesDto {
    private Long id;
    @NotBlank(message = "Tên Chuyên Khoa không được bỏ trống")
    private String name;
    @NotBlank(message = "Mô tả không được bỏ trống")
    private String description;
}
