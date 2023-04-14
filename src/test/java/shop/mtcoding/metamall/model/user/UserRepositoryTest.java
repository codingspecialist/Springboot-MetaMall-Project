package shop.mtcoding.metamall.model.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    void findByUsername() {
        // given
        userRepository.save(User.builder()
                .username("asdf")
                .password("1234")
                .email("asdf@naver.com")
                .role(Role.USER.getRole())
                .build());

        // when
        User user = userRepository.findByUsername("asdf").orElseThrow();

        // then
        Assertions.assertThat(user.getUsername()).isEqualTo("asdf");
    }
}