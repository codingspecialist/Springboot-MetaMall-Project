package shop.mtcoding.metamall.model.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAll() {
        //given

        //when
        List<Product> products = productRepository.findAll();

        //then
        assertEquals(2,products.size());
    }

    @Test
    void findById() {
        //given
        long id = 1;

        //when
        Product product = productRepository.findById(id).orElse(null);

        //then
        assertEquals(id,product.getId());
    }

    @Test
    void findByIdAndUser() {
        //given
        long id = 1;
        User seller = userRepository.findByUsername("seller1").orElse(null);

        //when
        Product product = productRepository.findByIdAndUser(id,seller).orElse(null);

        //then
        assertEquals(id,product.getId());
        assertEquals(seller,product.getSeller());
    }
}