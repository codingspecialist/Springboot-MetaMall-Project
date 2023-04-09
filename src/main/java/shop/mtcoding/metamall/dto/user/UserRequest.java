package shop.mtcoding.metamall.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserRequest {
    @Getter
    public static class LoginDto {
        @NotEmpty
        private final String username;
        @NotEmpty
        private final String password;

        @Builder
        public LoginDto(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    public static class SignUpDto{
        @NotEmpty
        private final String username;
        @NotEmpty
        private final String password;
        @NotEmpty
        @JsonProperty("password_check")
        private String passwordCheck;
        @NotEmpty
        @Pattern(regexp = "\\w+@\\w+\\.\\w{2,}")
        private final String email;
        @NotNull
        private final String role;

        @Builder
        public SignUpDto(String username, String password, String passwordCheck, String email, String role) {
            this.username = username;
            this.password = password;
            this.passwordCheck = passwordCheck;
            this.email = email;
            this.role = role;
        }
    }
}
