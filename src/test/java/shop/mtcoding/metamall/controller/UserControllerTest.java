package shop.mtcoding.metamall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUp() throws Exception {
        //given
        UserRequest.SignUpDto singup  = UserRequest.SignUpDto.builder()
                .username("test1")
                .email("test1@email.com")
                .password("1234")
                .passwordCheck("1234")
                .role("USER")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(singup);

        //when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/user/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actions = mockMvc.perform(builder);

        //then
        ResultMatcher isOk = MockMvcResultMatchers.status().isOk();
        actions.andExpect(isOk)
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void login() throws Exception {
        //given
        UserRequest.LoginDto login = UserRequest.LoginDto.builder()
                .username("ssar")
                .password("1234")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(login);

        //when
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/api/user/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actions = mockMvc.perform(builder);

        //then
        ResultMatcher isOk = MockMvcResultMatchers.status().isOk();
        actions.andExpect(isOk)
                .andDo(MockMvcResultHandlers.print());
    }
}