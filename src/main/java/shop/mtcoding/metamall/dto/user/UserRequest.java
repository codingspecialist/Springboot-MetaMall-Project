package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;

public class UserRequest {
    @Getter
    public static class LoginDto {
        private String username;
        private String password;
    }

    @Getter
    public static class JoinDto {
        private String username;
        private String password;
        private String email;
        private String role;
    }
}
