package shop.mtcoding.metamall.model.ordersheet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.metamall.model.user.Role;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
class OrderSheetRepositoryTest {
    @Autowired
    private OrderSheetRepository orderSheetRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder().username("jju").email("nn").password("1234").build(); //userId = 3
        user.setRole(Role.USER);
        userRepository.save(user);
        OrderSheet orderSheet = OrderSheet.builder().user(user).totalPrice(0).build();
        orderSheetRepository.save(orderSheet);
    }

    @Test
    void findByUserId() {
        //given
        Long userId = 3L;

        //when
        Optional<OrderSheet> orderSheetOP = orderSheetRepository.findByUserId(userId);

        //then
        String username = orderSheetOP.get().getUser().getUsername();
        Assertions.assertThat(username).isEqualTo("jju");
    }

    @Test
    void deleteByUserId() {
        //given
        Long userId = 2L;

        //when
        orderSheetRepository.deleteByUserId(userId);

        //then

        Assertions.assertThat(orderSheetRepository.findByUserId(userId)).isNotPresent();
    }
}