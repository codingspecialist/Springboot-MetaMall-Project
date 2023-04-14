package shop.mtcoding.metamall;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import shop.mtcoding.metamall.model.order.product.OrderProductRepository;
import shop.mtcoding.metamall.model.order.sheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.Arrays;

@SpringBootApplication
public class MetamallApplication {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, OrderSheetRepository orderSheetRepository) {
        return (args) -> {
            User seungmin = User.builder().username("ssar").password("1234").email("ssar@nate.com").role(Role.USER.getRole()).build();
            User seller = User.builder().username("seller").password("1234").email("seller@nate.com").role(Role.USER.getRole()).build();
            User admin = User.builder().username("admin").password("1234").email("admin@nate.com").role(Role.ADMIN.getRole()).build();
            userRepository.saveAll(Arrays.asList(seungmin, seller, admin));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(MetamallApplication.class, args);
    }

}
