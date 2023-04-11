package shop.mtcoding.metamall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.metamall.core.filter.AccessFilter;
import shop.mtcoding.metamall.model.log.error.ErrorLogRepository;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = OrderController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AccessFilter.class))
class OrderControllerTest {


    @MockBean
    private OrderSheetRepository orderSheetRepository;

    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private OrderProductRepository orderProductRepository;
    @MockBean
    private UserRepository userRepository;


    @MockBean
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("OrderController LIST GET TEST")
    void OrderControllerTest() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        OrderSheet o1 = OrderSheet.builder().totalPrice(300).build();
        OrderSheet o2 = OrderSheet.builder().totalPrice(400).build();

        // when
        given(orderSheetRepository.findAllData()).willReturn(Optional.of(List.of(o1,o2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/all/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].totalPrice").value(300))
                .andExpect(jsonPath("$.data[1].totalPrice").value(400));
    }

    @Test
    @DisplayName("OrderController MYPAGE GET TEST")
    void test() throws Exception {
        // given
        OrderSheet o1 = OrderSheet.builder().totalPrice(300).build();
        OrderSheet o2 = OrderSheet.builder().totalPrice(400).build();
        // when
        given(orderSheetRepository.findOrdData(anyString())).willReturn(Optional.of(List.of(o1,o2)));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/order/my")
                        .requestAttr("username","tester"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].totalPrice").value(300))
                .andExpect(jsonPath("$.data[1].totalPrice").value(400));
    }


    /**
     * 목 객체의 메소드 호출에 대한 예상을 설정할 때는 any()와 같은 Matcher(any())를 사용하여 원하는 형태의 인자를 전달해야한다.
     *  직접 객체를 넣을 경우 제대로 실행이 불가할 수 있다. Matcgher를 인자로 넣어야한다.
     * json 포맷을 모를 떄는 andExpect().andReturn().getResponse().getContentAsString()으로 뽑아보자
     * */
    @Test
    @DisplayName("OrderController POST TEST")
    void test3() throws Exception {
        // given
        List<OrderProduct> list = List.of(OrderProduct.builder().orderPrice(404)
                        .product(Product.builder().qty(10).build())
                        .productId(1L)
                        .build(),
                OrderProduct.builder()
                        .product(Product.builder().qty(10).build())
                        .productId(2L)
                        .orderPrice(500).build());


        OrderSheet orderSheet = OrderSheet.builder()
                .orderProductList(list)
                .totalPrice(400)
                .userId("tester")
                .build();

        OrderSheet orderSheet1 = OrderSheet.builder()
                .orderProductList(list)
                .id(1L)
                .totalPrice(400)
                .userId("tester")
                .build();

        Optional<Product> product = Optional.of(Product.builder().id(1L).qty(10).build());


        // when
        given(orderSheetRepository.save(any(OrderSheet.class))).willReturn(orderSheet1);
        given(productRepository.findById(anyLong())).willReturn(product);
        // then

        mockMvc.perform(MockMvcRequestBuilders.post("/order/save")
                .requestAttr("username","tester")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(list))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderProductList[0].product.qty").value(10))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.status").value(200));


    }

    @Test
    @DisplayName("DELETE REQUEST TEST")
    void test4() throws Exception {
        // given
        List<OrderProduct> list = List.of(OrderProduct.builder().orderPrice(404)
                        .product(Product.builder().qty(10).build())
                        .productId(1L)
                        .build(),
                OrderProduct.builder()
                        .product(Product.builder().qty(10).build())
                        .productId(2L)
                        .orderPrice(500).build());


        Optional<OrderSheet> orderSheet = Optional.of(
                OrderSheet.builder()
                        .id(1L)
                        .totalPrice(400)
                        .user(User.builder().username("test").build())
                        .orderProductList(list)
                        .build());

        // when
        given(orderSheetRepository.findByIdWithUser(any())).willReturn((Optional.of(
                        OrderSheet.builder()
                                .id(1L)
                                .totalPrice(400)
                                .user(User.builder().username("test").build())
                                .orderProductList(list)
                                .build())));

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/order/delete")
                        .requestAttr("username","ADMIN")
                        .requestAttr("role","SELLER")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.user.username").value("test"))
                .andExpect(jsonPath("$.data.orderProductList.length()").value(2))
        ;
    }

}