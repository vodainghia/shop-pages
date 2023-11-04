package springbootproject.shoppages.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private Long id;

    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotBlank(message = "Email should not be empty")
    @Email
    private String email;

    @Email
    private String targetEmail;

    @NotBlank(message = "Password should not be empty")
    private String password;

    @NotBlank(message = "Confirm Password should not be empty")
    private String confirmPassword;
}
