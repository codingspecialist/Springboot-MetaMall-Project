package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserRegister
{
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @Email
    private String email;
}
