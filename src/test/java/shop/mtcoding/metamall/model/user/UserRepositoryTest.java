package shop.mtcoding.metamall.model.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void findByUsername() {
        //given
        String username = "jju";
        User user = User.builder().username(username).email("nn").password("1234").build(); //userId = 3
        userRepository.save(user);
        //when
        User findUser = userRepository.findByUsername(username).get();
        //then
        System.out.println("find================" + findUser);
        Assertions.assertThat(findUser.getId()).isEqualTo(3);

    }
}