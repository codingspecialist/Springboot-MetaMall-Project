package shop.mtcoding.metamall.model.order.sheet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.metamall.model.order.product.OrderProductRepository;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest
class OrderSheetRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrderSheetRepository orderSheetRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE order_sheet_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE order_product_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE product_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    void findByUserId() {
        // given
        User userPS = userRepository.save(User.builder()
                .username("asdf")
                .password("1234")
                .email("asdf@naver.com")
                .role(Role.USER.getRole())
                .build());
//        OrderProduct orderProductPS = orderProductRepository.save(OrderProduct.builder()
//                .product(productRepository.save(Product.builder()
//                        .name("바나나")
//                        .price(3000)
//                        .qty(100)
//                        .build()))
//                .count(3)
//                .orderPrice(9000)
//                .build());
        OrderSheet orderSheet = OrderSheet.builder()
                .user(userPS)
                .totalPrice(9000)
                .build();
        OrderSheet orderSheetPS = orderSheetRepository.save(orderSheet);

//        orderSheetPS.addOrderProduct(orderProductPS);
//        orderProductPS.getProduct().updateQty(orderProductPS.getCount());
//        em.flush();

        Long id = 1L;

        // when
        List<OrderSheet> orderSheetListPS = orderSheetRepository.findByUserId(id);

        // then
        Assertions.assertThat(orderSheetListPS.get(0).getUser().getUsername()).isEqualTo("asdf");
    }
}