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
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    private static final String SUBJECT = "jwtstudy";
    private static final int EXP = 1000 * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    public static final String HEADER = "Authorization";
    private static final String SECRET = "메타코딩";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String jwt = "";

    @BeforeEach
    void setUp(){
//        User user = userRepository.findById(2L).get(); //ssar 유저 꺼내서 로그인시키기
        User user = userRepository.findById(1L).get(); //seller 유저 꺼내서 로그인시키기


        jwt = TOKEN_PREFIX + JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().toString())
                .sign(Algorithm.HMAC512(SECRET));
        //
    }


    @Test
    void find() throws Exception{
        //given
        String requestBody = "{\"name\":\"book1\"}"; //productDto 만들어서 전달

        //then
        MvcResult result = mockMvc.perform(post("/find")
                        .header(HEADER, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void findAll() throws Exception{
        //then
        MvcResult result = mockMvc.perform(get("/findAll")
                        .header(HEADER, jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void upload() throws Exception{
        //given
        String requestBody = "{\"name\":\"book3\",\"price\":\"100000\",\"qty\":\"10\"}";

        //then
        MvcResult result =  mockMvc.perform(post("/upload")
                        .header(HEADER, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void update() throws Exception{
        //given
        String requestBody = "{\"name\":\"book2\",\"price\":\"12345\",\"qty\":\"10\"}"; // 수정할 물건 내용 book2를 수정

        //then
        MvcResult result =  mockMvc.perform(put("/update/{productname}", "book2")
                        .header(HEADER, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

//    @Test
//    void delete() throws Exception{
//
//        //then
//         mockMvc.perform(delete("/delete/{productname}", "book2")) 이부분 다시
//                        .header(HEADER, jwt)
//                        .contentType(MediaType.APPLICATION_JSON)
//                .andExpect(status().isNoContent());
//
//    }
}