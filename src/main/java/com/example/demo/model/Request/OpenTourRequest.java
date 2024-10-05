package com.example.demo.model.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenTourRequest {
    @Min(value = 0, message = "Total price must be positive!")
    private BigDecimal totalPrice; // Đổi sang BigDecimal

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String status;

    // ID của Tour, có thể null nếu chưa có Tour tương ứng
    private Long tourId;
}

