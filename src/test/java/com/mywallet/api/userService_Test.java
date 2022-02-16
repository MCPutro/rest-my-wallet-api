package com.mywallet.api;

import com.mywallet.api.entity.User;
import com.mywallet.api.repository.UserRepository;
import com.mywallet.api.request.UserSignUpRequest;
import com.mywallet.api.response.UserResponse;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.service.FireBaseService;
import com.mywallet.api.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class userService_Test {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FireBaseService firebaseService;

    @Mock
    private PasswordEncoder encoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserServiceImpl(this.userRepository, this.firebaseService, encoder, null, null, null, null);
    }

    @Test
    void test_create_userSucccess() {
        UserSignUpRequest newUser = UserSignUpRequest.builder()
                .email("email-001@local.com")
                .password("123456789")
                .username("user local")
                .deviceId("adalah")
                .build();

        User u = new User(
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getUsername(),
                null,
                null
                );
        u.setDeviceId("adalah");
        u.setRegistrationDate(LocalDateTime.now());

        //User newUserr = new User();

        ResponseFormat rf = ResponseFormat.builder()
                .status(ResponseFormat.Status.success)
                .data(new UserResponse("uid", newUser.getUsername(), newUser.getEmail(), "url"))
                .build();

        //Mockito.when(this.userService.coba(Mockito.any(UserSignUpRequest.class))).thenReturn(rf);

        Mockito.when(this.userRepository.findByEmail(newUser.getEmail())).thenReturn(null);

        Mockito.when(this.firebaseService.createUser(Mockito.any(User.class))).thenReturn(rf);

        Mockito.when(this.userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArgument(0))
        ;

        ResponseFormat responseFormat = this.userService.insertUser(newUser);

        System.out.println(">> "+responseFormat.getStatus());

        Mockito.verify(this.userRepository, Mockito.times(1))
                .findByEmail(newUser.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));

        Mockito.verify(this.firebaseService, Mockito.times(1))
                .createUser(Mockito.any(User.class));

        Mockito.verify(encoder, Mockito.times(1)).encode(newUser.getPassword());

    }

    @Test
    void test_create_userFail() {
        UserSignUpRequest newUser = UserSignUpRequest.builder()
                .email("email-001@local.com")
                .password("123456789")
                .username("user local")
                .deviceId("adalah")
                .build();

        User u = new User(
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getUsername(),
                null,
                null
        );
        u.setDeviceId("adalah");
        u.setRegistrationDate(LocalDateTime.now());

        User existingUser = new User(
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getUsername(),
                null,
                null
        );

        Mockito.when(this.userRepository.findByEmail(newUser.getEmail())).thenReturn(existingUser);

        ResponseFormat coba = this.userService.insertUser(newUser);

        //System.out.println(coba.getStatus());

        Mockito.verify(this.userRepository, Mockito.times(1)).findByEmail(Mockito.any(String.class));

        Assertions.assertSame(ResponseFormat.Status.error, coba.getStatus());
    }
}
