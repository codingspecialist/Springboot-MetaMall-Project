package shop.minostreet.shoppingmall.restdocs;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.domain.UserEnum;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//테스트 코드를 쉽게 작성하기 위한 클래스로 관례적인 코드를 모아둔다.
@SpringBootTest
@AutoConfigureMockMvc
//웹에 대한 테스트를 모킹하기 위한 어노테이션
@ExtendWith(RestDocumentationExtension.class)
//RestDoc 짜기 위해 필요한 클래스를 빈으로 등록하기 위한 어노테이션
@Import(RestDocsConfiguration.class)
//Docs 설정 임포트 -> TestConfiguration 설정했고, 빈으로 등록했으므로 임포트 가능
public class TestSupport {
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                //매번 반복되는 코드를 추가
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                //JSON 포맷팅과 쉽게 작성하기 위한 클래스
                .build();
    }
    //하위 클래스에서 사용할 JSON 클래스
    protected String readJson(final String path) throws IOException {
        return IOUtils.toString(
                resourceLoader.getResource("classpath:" + path).getInputStream(),
                StandardCharsets.UTF_8
        );
    }

    protected User newUserAdmin(String username){
        BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
//                .password("1234")
                .password(encPassword)
                .email(username+"@nate.com")
                .role(UserEnum.ADMIN)
                .status(true)
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
                .status(true)
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
                .status(true)
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
