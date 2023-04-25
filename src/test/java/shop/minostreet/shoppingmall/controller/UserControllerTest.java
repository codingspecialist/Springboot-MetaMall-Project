package shop.minostreet.shoppingmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.minostreet.shoppingmall.config.dummy.DummyObject;
import shop.minostreet.shoppingmall.dto.user.UserReqDto.JoinReqDto;
import shop.minostreet.shoppingmall.repository.UserRepository;
import shop.minostreet.shoppingmall.restdocs.TestSupport;

import javax.persistence.EntityManager;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
//: dev 모드에서 발동하는 DummyInit의 유저가 삽입되므로
//@Transactional
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
//통합테스트
public class UserControllerTest extends TestSupport {
    @Autowired
    private ObjectMapper om;

    @Test
    public void join_success_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("love");
//        joinReqDto.setUsername("ssar"); //dev모드일 때 DummyInit 발동해서 오류 발생하는걸 확인하기위해
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("love@nate.com");

        //Object -> JSON
        String requestBody = om.writeValueAsString(joinReqDto);
        System.out.println("테스트 : " + requestBody);
        //when
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //컨텐트를 넣으면 반드시 컨텐트를 설명하는 컨텐트타입이 필요하다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isCreated()) //201
                .andDo(
                        restDocs.document(
                                //static import 진행
                                requestFields(
                                        //optional이 아니라 필수 값이므로
                                        fieldWithPath("username").description("username"),
                                        fieldWithPath("password").description("password"),
                                        fieldWithPath("email").description("email")
                                ),
                                //응답 필드에 대한 문서 작성
                                responseFields(
                                        fieldWithPath("code").description("code"),
                                        fieldWithPath("msg").description("msg"),
                                        fieldWithPath("data").description("data object"),
                                        fieldWithPath("data.id").description("id"),
                                        fieldWithPath("data.username").description("username")
                                )
                        )
                );
    }


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    //    @BeforeEach
//    public void setUp(){
//        dataSetting();
//    }
//
//    private void dataSetting() {
//        //DummyObject의 newUser()
//        userRepository.save(newUser("ssar", "pepe ssar"));
//    }
//
    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("ssar"));
        em.clear();
    }


    @Test
    //통합테스트이므로, 서비스단에서 중복체크에서 예외가 발생하는 경우 테스트
    //: @BeforeEach로 미리 해당 유저를 생성
    public void join_fail_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("love@nate.com");

        //Object -> JSON
        String requestBody = om.writeValueAsString(joinReqDto);
        System.out.println("테스트 : " + requestBody);
        //when
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //컨텐트를 넣으면 반드시 컨텐트를 설명하는 컨텐트타입이 필요하다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("msg").description("message"),
                                fieldWithPath("data").description("Data Object")
                        )));  //400
//        resultActions.andExpect(status().isCreated());  //201
//        resultActions.andExpect(status().isOk());   //200
    }
}
