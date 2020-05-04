package com.klever.bok.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
public class SignupRequest {

    @NotBlank(message = "Name may not be blank")
    @Size(min = 2, max = 20, message = "Name '${validatedValue}' must be between {min} and {max} characters long")
    private String name;

    @NotBlank(message = "Lastname may not be blank")
    @Size(min = 2, max = 20, message = "Lastname '${validatedValue}' must be between {min} and {max} characters long")
    private String lastname;

    @NotBlank(message = "Username may not be blank")
    @Size(min = 4, max = 20, message = "Username '${validatedValue}' must be between {min} and {max} characters long")
    private String username;

    @NotBlank(message = "Email may not be blank")
    @Size(min = 5, max = 50, message = "Email '${validatedValue}' must be between {min} and {max} characters long")
    @Email(message = "Email '${validatedValue}' must be a well-formed email address")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Password may not be blank")
    @Size(min = 4, max = 120, message = "Password must be between {min} and {max} characters long")
    private String password;
}
