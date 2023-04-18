package shop.minostreet.shoppingmall.config.dummy;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.domain.UserEnum;

public class DummyObject {
    //모두 스태틱 메서드
    protected User newUserAdmin(String username){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
//                .password("1234")
                .password(encPassword)
                .email(username+"@nate.com")
                .role(UserEnum.ADMIN)
                .build();
    }
    protected User newUser(String username){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
//                .password("1234")
                .password(encPassword)
                .email(username+"@nate.com")
                .role(UserEnum.CUSTOMER)
                .build();
    }
    protected static User newMockUser(Long id,String username){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
//                .password("1234")
                .password(encPassword)
                .email(username+"@nate.com")
                .role(UserEnum.CUSTOMER)
                .build();
    }

    protected static Product newProduct(String name, Integer qty, Integer price){
        return Product.builder()
                .name(name)
                .qty(qty)
                .price(1000)
                .build();
    }

    protected static Product newMockProduct(Long id,String name, Integer qty, Integer price) {
        return Product.builder()
                .id(id)
                .name(name)
                .qty(qty)
                .price(price)
                .build();
    }
    


}
