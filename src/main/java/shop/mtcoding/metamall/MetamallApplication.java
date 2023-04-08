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

@SpringBootApplication
public class MetamallApplication {

	@Bean
	CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, OrderSheetRepository orderSheetRepository){
		return (args)->{
			// 여기에서 save 하면 됨.
			// bulk Collector는 saveAll 하면 됨.
			User ssar = User.builder().username("user").password("1234").email("ssar@nate.com").role("USER").build();
			userRepository.save(ssar);

			User mook = User.builder().username("seller").password("1234").email("ssar@nate.com").role("SELLER").build();
			userRepository.save(mook);

			Product product = Product.builder().name("Apple").price(1000).qty(10).build();
			productRepository.save(product);

			Product product2 = Product.builder().name("Kiwi").price(2000).qty(20).build();
			productRepository.save(product2);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(MetamallApplication.class, args);
	}

}
