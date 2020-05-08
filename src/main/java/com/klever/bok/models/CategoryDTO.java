package com.klever.bok.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 8033378299659935378L;

    @NotBlank(message = "Name may not be blank")
    @Size(min = 1, max = 100, message = "Name '${validatedValue}' must be between {min} and {max} characters long")
    private String name;

    private UUID parentCategoryId;
}
