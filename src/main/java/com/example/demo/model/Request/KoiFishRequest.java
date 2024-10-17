package com.example.demo.model.Request;

import com.example.demo.model.Response.AdditionEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class KoiFishRequest {
    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Name not have number and first character not have space!")
    private String name;

    @NotBlank(message = "Name can not be blank")
    @Pattern(regexp = "^[^\\d\\s].*", message = "Name not have number and first character not have space!")
    private String description;

    private String image;

    long breedId;
    long farmId;

}
