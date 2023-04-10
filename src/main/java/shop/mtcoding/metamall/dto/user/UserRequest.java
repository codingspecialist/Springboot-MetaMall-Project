package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

import java.time.LocalDateTime;

public class UserRequest {
    @Getter @Setter
    public static class LoginDto {
        private String username;
        private String password;
    }
    @Getter
    @Setter
    public static class JoinDto{
        private String username;
        private String password;
        private String email;
        private String role; // USER(고객), SELLER(판매자), ADMIN(관리자)


    }
}
