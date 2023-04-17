package shop.mtcoding.metamall.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class UserRequest {
    @Getter @Setter
    public static class LoginDto {
        private String username;
        private String password;
    }

    @Getter @Setter
    public static class JoinDto {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;
        @NotEmpty
        private String role;
    }
}
