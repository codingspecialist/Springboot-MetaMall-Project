package shop.mtcoding.metamall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    //회원가입
    @Transactional
    public Long join(User user)
    {
        return user.getId();
    }

}
