package com.altun.fiveten.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @NotNull(message = "Username can not be null")
    @Size(min = 2, max = 20, message = "Username should contain at least 2 characters and maximum 20 characters")
    private String username;
    @NotNull(message = "Password can not be null")
    @Size(min = 2, message = "Password should have at least 2 characters")
    private String password;

    @NotNull(message = "Longitude can not be null")
    private double longitude;
    @NotNull(message = "Latitude can not be null")
    private double latitude;
}
