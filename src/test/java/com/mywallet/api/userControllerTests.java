package com.mywallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mywallet.api.config.jwt.JwtAuthenticationEntryPoint;
import com.mywallet.api.config.jwt.JwtTokenUtil;
import com.mywallet.api.controller.UserController;
import com.mywallet.api.request.UserSignInRequest;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.service.JwtUserDetailsService;
import com.mywallet.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class)
public class userControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void Test_CreateUser() throws Exception{
//        UserSignUpRequest newUser = UserSignUpRequest.builder()
//                .email("email1@local.com")
//                .password("123456789")
//                .username("qwerty")
//                .deviceId("haha")
//                .build();
//
//        mockMvc.perform(
//                post("/api/user/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newUser))
//                )
//                .andExpect(jsonPath("$.status", is("success")))
//        ;
        UserSignInRequest signInRequest = UserSignInRequest.builder()
                .email("email@local.com1")
                .password("123456789")
                .build();

        ResponseFormat rf = ResponseFormat.builder()
                .status(ResponseFormat.Status.success)
                .build();

        //Mockito.when(userService.signIn(signInRequest)).thenReturn(rf);

        this.mockMvc.perform(
                post("/api/user/signin")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signInRequest)))
                //.andExpect(jsonPath("$.data.uid", notNullValue() ))
                .andReturn()
        ;

        ;


    }
}
