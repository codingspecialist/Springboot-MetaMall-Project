package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {

    @Getter @Setter
    public static class JoinDTO {

        // Size는 String에만 쓸 수 있다.
        @NotEmpty
        @Size(min = 3, max = 20)
        private String username;

        @NotEmpty
        @Size(min = 4, max = 20) // DB에는 60자, 실제 받을 때는 20자 이하
        private String password;

        // Pattern은 String에만 쓸 수 있다.
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 아닙니다")
        private String email;

        @NotEmpty
        @Pattern(regexp = "USER|SELLER|ADMIN")
        private String role;

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
    public static class LoginDTO {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Getter @Setter
    public static class RoleUpdateDTO {
        @NotEmpty
        private String role;
    }

}