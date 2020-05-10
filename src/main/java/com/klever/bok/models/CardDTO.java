package com.klever.bok.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 8421430678058499579L;

    @NotBlank(message = "Title may not be blank")
    @Size(min = 1, max = 100, message = "Title '${validatedValue}' must be between {min} and {max} characters long")
    private String title;

    @NotBlank(message = "Content may not be blank")
    private String content;

    @NotNull(message = "Category Id may not be Null")
    private UUID categoryId;

    private Set<UUID> tagIds;
}
