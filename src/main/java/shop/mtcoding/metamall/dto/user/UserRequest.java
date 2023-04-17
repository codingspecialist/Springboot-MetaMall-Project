package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {
    @Getter @Setter
    public static class LoginDto {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Getter @Setter
    public static class JoinDTO {
        @NotEmpty
        @Size(min = 3, max = 20)
        private String username;

        @NotEmpty
        @Size(min = 4, max = 20)
        private String password;

        @NotEmpty
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$", message = "이메일 형식이 아닙니다")
        private String email;

        @NotEmpty
        @Pattern(regexp = "USER|SELLER|ADMIN")
        private String role;

        // Insert DTO만 toEntity를 만들면 된다
        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .status(true)
                    .build();
        }
    }

    @Getter @Setter
    public static class RoleUpdateDTO {

        @Pattern(regexp = "USER|SELLER|ADMIN")
        @NotEmpty
        private String role;
    }
}
