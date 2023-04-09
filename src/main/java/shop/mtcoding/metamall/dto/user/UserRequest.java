package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

import javax.validation.constraints.NotEmpty;

public class UserRequest {

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

        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .build();
        }
    }

    @Getter @Setter
    public static class LoginDto {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Getter @Setter
    public static class RoleUpdateDto {
        private String role;
    }

}
