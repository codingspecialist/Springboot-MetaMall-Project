package shop.mtcoding.metamall.dto.user;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

public class UserRequest {
    @Data
    public static class LoginDto {
        private String username;
        private String password;
    }

    @Data
    public static class JoinDto {
        @NotNull // **예외처리 해주기
        private String username;
        @NotNull
        private String password;
        @NotNull
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }
    }
}
