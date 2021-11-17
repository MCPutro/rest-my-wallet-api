package com.mywallet.api.config.jwt;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		Exception exception = (Exception) request.getAttribute("exception");

		String message;
		Map<String, String> resp = new LinkedHashMap<>();
		resp.put("status", "error");

		if (exception != null) {

			if (exception.getCause() != null) {
				message = exception.getCause().toString() + " " + exception.getMessage();
			} else {
				message = exception.getMessage();
			}

			resp.put("message", message);

			byte[] body = new ObjectMapper().writeValueAsBytes(resp);

			response.getOutputStream().write(body);

		} else {

			if (authException.getCause() != null) {
				message = authException.getCause().toString() + " " + authException.getMessage();
			} else {
				message = authException.getMessage();
			}

			resp.put("message", message);

			byte[] body = new ObjectMapper().writeValueAsBytes(resp);

			response.getOutputStream().write(body);
		}
	}
}