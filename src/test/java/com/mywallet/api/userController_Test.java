package com.mywallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mywallet.api.config.jwt.JwtAuthenticationEntryPoint;
import com.mywallet.api.config.jwt.JwtTokenUtil;
import com.mywallet.api.controller.UserController;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.request.UserSignInRequest;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.service.JwtUserDetailsService;
import com.mywallet.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class)
public class userController_Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.initMocks(this);
//        this.userService = new UserServiceImpl();
    }

    @Test
    void Test_SignInUser() throws Exception{
        UserSignInRequest signInRequest = UserSignInRequest.builder()
                .email("email@local.com")
                .password("123456789")
                .build();

        ResponseFormat rf = ResponseFormat.builder()
                .status(ResponseFormat.Status.success)
                .build();

        Mockito.when(userService.signIn(signInRequest)).thenReturn(rf);

        this.mockMvc.perform(
                post("/api/user/signin")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(jsonPath("$.status", is("success") ))
        ;

        Mockito.verify(userService, Mockito.times(1))
                .signIn(signInRequest);

    }


    @Test
    void Test_CreateUser() throws Exception {

    }
}
