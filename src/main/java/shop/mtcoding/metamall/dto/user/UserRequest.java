package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UserRequest {
    @Getter @Setter
    public static class LoginDto {
        @NotBlank(message = "고객명 입력은 필수입니다")
        private String username;
        @NotBlank(message = "비밀번호 입력은 필수입니다")
        private String password;
    }
}
