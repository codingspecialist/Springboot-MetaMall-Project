package shop.mtcoding.metamall.model.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        //given
        String username = "ssar";

        //when
        User user = userRepository.findByUsername(username).orElse(null);

        //then
        assertEquals(username,user.getUsername());
    }
}