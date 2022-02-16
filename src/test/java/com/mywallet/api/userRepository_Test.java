package com.mywallet.api;

import com.mywallet.api.entity.User;
import com.mywallet.api.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class userRepository_Test {

    @Autowired private UserRepository userRepository;

    @Test
    void TestInsert() {
        User u = new User();
        u.setEmail("haha");
        u.setPassword("hahaha1");
        u.setUsername("hahaha");
        u.setDeviceId("hahaha");
        u.setUrlAvatar("hahaha");
        u.setRegistrationDate(LocalDateTime.now());

        User save = userRepository.save(u);

        System.out.println(">>>>>"+save.getId());

        Assertions.assertNotNull(save);

    }

}
