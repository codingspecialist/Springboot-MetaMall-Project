package shop.mtcoding.metamall.config.dummy;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shop.mtcoding.metamall.domain.Product;
import shop.mtcoding.metamall.domain.User;
import shop.mtcoding.metamall.repository.ProductRepository;
import shop.mtcoding.metamall.repository.UserRepository;


@Configuration
public class DummyInit extends DummyObject{

    //빈 메서드에서 프로퍼티 설정
    @Profile("dev")
    @Bean
    //설정 클래스에 있는 빈메서드는 서버 실행시 무조건 실행
    //: DI 두 번째 방법 메서드 주입
    CommandLineRunner init(UserRepository userRepository, ProductRepository productRepository){
        return (args) -> {
            User ssar = userRepository.save(newUser("ssar"));
            User cos = userRepository.save(newUser("cos"));
            Product product1=productRepository.save(newProduct("시계", 2, 5000));
            Product product2=productRepository.save(newProduct("자물쇠", 5, 3000));
        };
    }
}