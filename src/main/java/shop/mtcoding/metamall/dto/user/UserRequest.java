package shop.mtcoding.metamall.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {
    @Getter @Setter
    public static class JoinDTO{
        @NotEmpty @Size(min=3, max=20)
        private String username;
        @NotEmpty @Size(min=4, max=20)
        private String password;
        @NotEmpty @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 아닙니다")
        private String email;
        @NotEmpty @Pattern(regexp = "USER|SELLER|ADMIN")
        private String role;

        //insert DTO만 toEntity를 만들어준다. update할때는 더티체킹
        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
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
        @Pattern(regexp = "USER|SELLER|ADMIN")
        @NotEmpty
        private String role;
    }
}
