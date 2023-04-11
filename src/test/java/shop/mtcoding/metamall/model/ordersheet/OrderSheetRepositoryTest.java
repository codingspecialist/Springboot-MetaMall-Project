package shop.mtcoding.metamall.model.ordersheet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
class OrderSheetRepositoryTest {

    @Autowired
    public OrderSheetRepository orderSheetRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public OrderProductRepository orderProductRepository;



    @BeforeEach
    void setUp(){

        User user = userRepository.save(User.builder().username("customer").password("1234").email("admin@nate.com").role("CUSTOMER").build());

        Product product = productRepository.findById(1L).get();
        OrderSheet orderSheet = orderSheetRepository.save(OrderSheet.builder().totalPrice(40).userId(user.getUsername()).build());
        orderProductRepository.save(OrderProduct.builder().product(product).count(30).orderSheet(orderSheet).build());
    }


    @Test
    void test(){
       // orderSheetRepository.findOrderSheetByUsernameOptional("customer").get().forEach(System.out::println);
    }
}