package shop.minostreet.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//Audit 기능 사용하기 위한 어노테이션 3
@EnableJpaAuditing
@SpringBootApplication
public class ShoppingmallApplication {

//	@Bean
//	CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, OrderSheetRepository orderSheetRepository){
//		return (args)->{
//			// 여기에서 save 하면 됨.
//			// bulk Collector는 saveAll 하면 됨.
//			User ssar = User.builder().username("ssar").password("1234").email("ssar@nate.com").role(UserEnum.CUSTOMER).build();
//			userRepository.save(ssar);
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(ShoppingmallApplication.class, args);
	}

}
