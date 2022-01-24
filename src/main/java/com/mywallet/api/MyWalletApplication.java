package com.mywallet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
public class MyWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyWalletApplication.class, args);
	}

//	@Bean
//	RouterFunction<ServerResponse> routerFunction() {
//		return route(GET("/doc"), req ->
//				ServerResponse.temporaryRedirect(URI.create("swagger-ui.html")).build());
//	}
}
