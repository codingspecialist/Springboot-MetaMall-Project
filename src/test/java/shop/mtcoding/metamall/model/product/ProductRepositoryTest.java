package shop.mtcoding.metamall.model.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    void findByName() {
        //given
        String productname = "phone";
        Product product = Product.builder().productname(productname).price(1000).qty(10).build(); // id : 3
        productRepository.save(product);
        //when
        Product findProduct = productRepository.findByName(productname).get();
        //then
        System.out.println("find ============ " + findProduct);
        Assertions.assertThat(findProduct.getId()).isEqualTo(3);
    }
}