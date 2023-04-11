package shop.mtcoding.metamall.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    private HttpHeaders headers(User user){
        String jwt = JwtProvider.create(user);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        requestHeaders.add(JwtProvider.HEADER,jwt);

        return requestHeaders;
    }

    @Test
    @DisplayName("상품 등록")
    void registerProduct(){
        //given
        User seller1 = userRepository.findByUsername("seller1").orElse(null);
        HttpHeaders headers = headers(seller1);
        ProductRequest.ProductDto product = ProductRequest.ProductDto.builder()
                .name("제품3")
                .price(100000)
                .qty(1)
                .build();
        HttpEntity<?> requestEntity = new HttpEntity<>(product, headers);

        //when

        ResponseEntity<?> response = testRestTemplate
                .postForEntity(
                        "/api/product",
                        requestEntity,
                        ResponseDto.class
                );

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("상품 조회")
    void getProducts() {
        //given
        User ssar = userRepository.findByUsername("ssar").orElse(null);
        HttpHeaders headers = headers(ssar);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        //when

        ResponseEntity<?> response = testRestTemplate
                .exchange(
                        "/api/products",
                        HttpMethod.GET,
                        requestEntity,
                        ResponseDto.class
                );

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("상품 상세")
    void getProductDetails() {
        //given
        long id = 1;
        User ssar = userRepository.findByUsername("ssar").orElse(null);
        HttpHeaders headers = headers(ssar);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        //when

        ResponseEntity<?> response = testRestTemplate
                .exchange(
                        "/api/product/"+id,
                        HttpMethod.GET,
                        requestEntity,
                        ResponseDto.class
                );

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct() {
        //given
        long id = 1;
        User seller1 = userRepository.findByUsername("seller1").orElse(null);
        HttpHeaders headers = headers(seller1);
        ProductRequest.ProductDto product = ProductRequest.ProductDto.builder()
                .name("제품3")
                .price(10000)
                .qty(1)
                .build();
        HttpEntity<?> requestEntity = new HttpEntity<>(product, headers);

        //when

        ResponseEntity<?> response = testRestTemplate
                .exchange(
                        "/api/product/"+id,
                        HttpMethod.PUT,
                        requestEntity,
                        ResponseDto.class
                );

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}