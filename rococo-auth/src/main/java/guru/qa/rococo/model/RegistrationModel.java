
package guru.qa.rococo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@EqualPasswords
public record RegistrationModel(
        @NotNull(message = "Username cannot be null")
        @NotEmpty(message = "Username cannot be empty")
        @Size(max = 50, message = "Username cannot be longer than 50 characters")
        String username,

        @NotNull(message = "Password cannot be null")
        @Size(min = 3, max = 12, message = "Password length must be between 3 and 12 characters")
        String password,

        @NotNull(message = "Password confirmation cannot be null")
        String passwordSubmit) {
}
