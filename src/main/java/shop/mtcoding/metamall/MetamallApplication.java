package shop.mtcoding.metamall;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import shop.mtcoding.metamall.repository.OrderProductRepository;
import shop.mtcoding.metamall.repository.OrderSheetRepository;
import shop.mtcoding.metamall.repository.ProductRepository;
import shop.mtcoding.metamall.domain.User;
import shop.mtcoding.metamall.domain.UserEnum;
import shop.mtcoding.metamall.repository.UserRepository;
//Audit 기능 사용하기 위한 어노테이션 3
@EnableJpaAuditing
@SpringBootApplication
public class MetamallApplication {

	@Bean
	CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, OrderSheetRepository orderSheetRepository){
		return (args)->{
			// 여기에서 save 하면 됨.
			// bulk Collector는 saveAll 하면 됨.
			User ssar = User.builder().username("ssar").password("1234").email("ssar@nate.com").role(UserEnum.CUSTOMER).build();
			userRepository.save(ssar);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(MetamallApplication.class, args);
	}

}
