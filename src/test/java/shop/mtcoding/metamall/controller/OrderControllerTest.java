package shop.mtcoding.metamall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    private static final String SUBJECT = "jwtstudy";
    private static final int EXP = 1000 * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    public static final String HEADER = "Authorization";
    private static final String SECRET = "메타코딩";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderSheetRepository orderSheetRepository;

    private String jwt = "";


    @BeforeEach
    @Transactional
    void setUp() {
        User user = userRepository.findById(2L).get(); //ssar 유저 꺼내서 로그인시키기
//        User user = userRepository.findById(1L).get(); //seller 유저 꺼내서 로그인시키기

        jwt = TOKEN_PREFIX + JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().toString())
                .sign(Algorithm.HMAC512(SECRET));

        // ssar의 book1 주문 만들기, seller는 안됨 ordersheet 없음
        Product productPS = productRepository.findByName("book1").get();

        productPS.setQty(productPS.getQty() - 10); //10개 주문, 수량 업데이트
        OrderProduct orderProduct = OrderProduct.builder().product(productPS).count(10).orderPrice(productPS.getPrice() * 10).build();
        orderProductRepository.save(orderProduct);

        OrderSheet orderSheetPS = orderSheetRepository.findByUserId(user.getId()).get();

        List<OrderProduct> orderProductList = orderSheetPS.getOrderProductList();
        orderProductList.add(orderProduct);
        orderSheetPS.setOrderProductList(orderProductList); //orderProduct 추가된 리스트로 업데이트
//
        Integer totalPrice = orderSheetPS.getTotalPrice();
        totalPrice = totalPrice + orderProduct.getOrderPrice();
        orderSheetPS.setTotalPrice(totalPrice);

        orderProduct.setOrderSheet(orderSheetPS.getId());

        User seller = userRepository.findById(1L).get(); //seller 유저 꺼내서 로그인시키기

        jwt = TOKEN_PREFIX + JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", seller.getId())
                .withClaim("role", seller.getRole().toString())
                .sign(Algorithm.HMAC512(SECRET));
    }

    @Test
    void order() throws Exception{
        //given
        String requestBody = "{\"name\":\"book1\",\"count\":\"10\"}"; //OrderDto 만들어서 전달

        //then
        MvcResult result = mockMvc.perform(post("/order")
                        .header(HEADER, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    @Transactional
    void findAllOrder() throws Exception{
        //then
        MvcResult result = mockMvc.perform(get("/findAllOrder")
                        .header(HEADER, jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    @Transactional
    void cancel() throws Exception{
        MvcResult result = mockMvc.perform(delete("/cancel")
                        .header(HEADER, jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    @Transactional
    void cancelSeller() throws Exception{
        MvcResult result = mockMvc.perform(delete("/cancel/{userId}", 2L) //ssar의 주문 취소
                        .header(HEADER, jwt)) //Role이 Seller여야 함
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
}