package shop.mtcoding.metamall.model.ordersheet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class OrderSheetRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private OrderSheetRepository orderSheetRepository;

    @Test
    void findByUser() {
        //given
        User user = userRepository.findByUsername("ssar").orElse(null);

        //when
        List<OrderSheet> orderSheet = orderSheetRepository.findByUser(user);

        //then
        assertEquals(1,orderSheet.size());
        assertEquals(user,orderSheet.get(0).getUser());
    }

    @Test
    void findBySeller() {
        //given
        User seller = userRepository.findByUsername("seller1").orElse(null);

        //when
        List<OrderSheet> orderSheet = orderSheetRepository.findBySeller(seller);

        //then
        assertEquals(1,orderSheet.size());
    }

    @Test
    void findByIdAndUser() {
        //given
        long id = 1;
        User user = userRepository.findByUsername("ssar").orElse(null);

        //when
        OrderSheet orderSheet = orderSheetRepository.findByIdAndUser(id,user).orElse(null);

        //then
        assertEquals(1,orderSheet.getId());
        assertEquals(user,orderSheet.getUser());
    }

    @Test
    void findByIdOrSeller(){
        //given
        long id = 1;
        User seller = userRepository.findByUsername("seller1").orElse(null);

        //when
        OrderSheet orderSheet = orderSheetRepository.findByIdOrSeller(id,seller).orElse(null);

        //then
        assertEquals(id,orderSheet.getId());
    }
}