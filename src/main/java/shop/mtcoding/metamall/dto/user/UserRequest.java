package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

public class UserRequest {
    @Getter
    @Setter
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
        private String role;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginDTO {
        private String username;
        private String password;
    }
}
