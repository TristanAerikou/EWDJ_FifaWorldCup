package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lv.ewdj.fifaworldcup.validator.ValidPasswords;

@ValidPasswords
public record InputRegistrationDto(
        @Pattern(regexp = "^[a-zA-Z]+$", message = "username must be alphanumeric with no spaces")
        @Size(min = 4, max = 25)
        String username,

        @NotBlank
        @Size(min = 4, max = 20)
        String password,

        @NotBlank
        String confirmPassword,

        @Pattern(regexp = "^[a-zA-Z]+$", message = "username must be alphanumeric with no spaces")
        String firstname,
        @Pattern(regexp = "^[a-zA-Z]+$", message = "username must be alphanumeric with no spaces")
        String lastname
) {

}
