package shop.mtcoding.metamall.core.session;

import lombok.Builder;
import lombok.Getter;
import shop.mtcoding.metamall.model.user.Role;

@Getter
public class LoginUser {
    private Long id;
    private String role;

    @Builder
    public LoginUser(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
