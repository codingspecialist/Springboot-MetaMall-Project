package shop.mtcoding.metamall;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.stream.IntStream;

@SpringBootApplication
public class MetamallApplication {

	@Bean
	CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, OrderSheetRepository orderSheetRepository){
		return (args)->{
			IntStream.rangeClosed(1,50).forEach(value -> {
				productRepository.save(Product.builder()
								.price(300+value)
								.name("TEst"+value)
								.qty(value)
						.build());
			});

			User ssar = User.builder().username("ssar").password("1234").email("ssar@nate.com").role("USER").build();
			userRepository.save(ssar);
			userRepository.save(User.builder().username("admin").password("1234").email("admin@nate.com").role("SELLER").build());
			userRepository.save(User.builder().username("customer").password("1234").email("admin@nate.com").role("CUSTOMER").build());
		};


	}

	public static void main(String[] args) {
		SpringApplication.run(MetamallApplication.class, args);
	}

}
