package shop.mtcoding.metamall.core.session;

import lombok.Builder;
import lombok.Getter;
import shop.mtcoding.metamall.model.user.User;

@Getter
public class LoginUser {
    private Long id;
    private User.Role role;

    @Builder
    public LoginUser(Long id, User.Role role) {
        this.id = id;
        this.role = role;
    }
}
