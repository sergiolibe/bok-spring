package com.klever.bok.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO implements Serializable {

    private static final long serialVersionUID = 5353332972901545510L;

    @NotBlank(message = "Name may not be blank")
    @Size(min = 1, max = 30, message = "Name '${validatedValue}' must be between {min} and {max} characters long")
    private String name;

    @NotBlank(message = "Color may not be blank")
    @Size(max = 7, message = "Color '${validatedValue}' must be between {min} and {max} characters long")
    private String color;
}
