package shop.mtcoding.metamall.model.ordersheet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class OrderSheetRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderSheetRepository orderSheetRepository;

    @Test
    void findAll(){
        //given

        //when
        List<OrderSheet> orderSheets = orderSheetRepository.findAll();

        //then
        assertEquals(2,orderSheets.size());
        assertEquals("ssar",orderSheets.get(0).getUser().getUsername());
        assertEquals("tester",orderSheets.get(1).getUser().getUsername());
    }

    @Test
    void findByUser(){
        //given
        User user = userRepository.findByUsername("ssar").orElse(null);

        //when
        List<OrderSheet> orderSheet = orderSheetRepository.findByUser(user);

        //then
        assertEquals(1,orderSheet.size());
        assertEquals(2,orderSheet.get(0).getOrderProductList().size());
        assertEquals(user,orderSheet.get(0).getUser());
    }

    @Test
    void findBySeller() throws JsonProcessingException {
        //given
        User seller = userRepository.findByUsername("seller1").orElse(null);

        //when
        List<OrderSheet> orderSheet = orderSheetRepository.findBySeller(seller);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        for(OrderSheet sheet : orderSheet){
            System.out.println(om.writeValueAsString(sheet));
        }

        //then
        assertEquals(2,orderSheet.size());
        assertEquals(1,orderSheet.get(0).getOrderProductList().size());
        assertEquals(seller,orderSheet.get(0).getOrderProductList().get(0).getProduct().getSeller());
        assertEquals("ssar",orderSheet.get(0).getUser().getUsername());
        assertEquals("tester",orderSheet.get(1).getUser().getUsername());
    }

    @Test
    void findByIdAndUser() {
        //given
        long id = 1;
        User user = userRepository.findByUsername("ssar").orElse(null);

        //when
        OrderSheet orderSheet = orderSheetRepository.findByIdAndUser(id,user).orElse(null);

        //then
        assertEquals(1,orderSheet.getId());
        assertEquals(user,orderSheet.getUser());
    }

    @Test
    void findByIdAndSeller(){
        //given
        long id = 1;
        User seller = userRepository.findByUsername("seller1").orElse(null);

        //when
        OrderSheet orderSheet = orderSheetRepository.findByIdAndSeller(id,seller).orElse(null);

        //then
        assertEquals(id,orderSheet.getId());
    }
}