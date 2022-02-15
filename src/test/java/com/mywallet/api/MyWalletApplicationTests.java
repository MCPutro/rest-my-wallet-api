package com.mywallet.api;

import com.mywallet.api.request.UserSignInRequest;
import com.mywallet.api.response.format.ResponseFormat;
import com.mywallet.api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWalletApplicationTests {

	@Autowired private UserService userService;

	@Test
	void loginUserTests() {
		UserSignInRequest signInRequest = UserSignInRequest.builder()
				.email("email@local.com1")
				.password("123456789")
				.build();

		ResponseFormat responseFormat = this.userService.signIn(signInRequest);

		Assertions.assertSame(responseFormat.getStatus(), ResponseFormat.Status.error);
	}

}
