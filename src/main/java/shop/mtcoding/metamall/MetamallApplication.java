package shop.mtcoding.metamall;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class MetamallApplication {

	@Bean
	CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository, OrderSheetRepository orderSheetRepository){
		return (args)->{
			// 여기에서 save 하면 됨.
			// bulk Collector는 saveAll 하면 됨.
			User ssar = User.builder().username("ssar").password("1234").email("ssar@nate.com").role(User.Role.USER).build();
			User tester = User.builder().username("tester").password("1234").email("tester@nate.com").role(User.Role.USER).build();
			User seller1 = User.builder().username("seller1").password("5678").email("seller1@email.com").role(User.Role.SELLER).build();
			User seller2 = User.builder().username("seller2").password("5678").email("seller2@email.com").role(User.Role.SELLER).build();
			User admin = User.builder().username("admin").password("0000").email("admin@email.com").role(User.Role.ADMIN).build();
			List<User> users = Stream.of(ssar,tester,seller1,seller2,admin).collect(Collectors.toList());
			userRepository.saveAll(users);

			Product product1 = Product.builder().user(seller1).name("제품1").qty(3).price(10000).build();
			Product product2 = Product.builder().user(seller2).name("제품2").qty(3).price(1000).build();
			List<Product> products = Stream.of(product1,product2).collect(Collectors.toList());
			productRepository.saveAll(products);

			orderSheetSave(product1,product2,ssar,productRepository,orderSheetRepository);
			orderSheetSave(product1,product2,tester,productRepository,orderSheetRepository);
		};
	}

	private void orderSheetSave(Product product1, Product product2, User user, ProductRepository productRepository, OrderSheetRepository orderSheetRepository){
		product1.order(1);
		product2.order(1);
		productRepository.saveAll(Arrays.asList(product1,product2));
		List<OrderProduct> orders = Arrays.asList(
				OrderProduct.builder().product(product1).count(1).orderPrice(product1.getPrice()).build(),
				OrderProduct.builder().product(product2).count(1).orderPrice(product2.getPrice()).build());
		int totalPrice = orders.stream().mapToInt(OrderProduct::getOrderPrice).sum();
		OrderSheet orderSheet = OrderSheet.builder().user(user).totalPrice(totalPrice).build();
		orders.forEach(order -> order.syncOrderSheet(orderSheet));
		orderSheetRepository.save(orderSheet);
	}
	public static void main(String[] args) {
		SpringApplication.run(MetamallApplication.class, args);
	}

}
