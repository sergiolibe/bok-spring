package com.klever.bok.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "Username may not be blank")
    @Size(min = 4, max = 20, message = "Username '${validatedValue}' must be between {min} and {max} characters long")
    private String username;

    @NotBlank(message = "Password may not be blank")
    @Size(min = 4, max = 120, message = "Password must be between {min} and {max} characters long")
    private String password;
}
