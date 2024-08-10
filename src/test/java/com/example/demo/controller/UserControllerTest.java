package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.domain.user.controller.UserController;
import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.LoginResponseDto;
import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.config.WebSecurityConfig;
import com.example.demo.global.util.JwtUtil;
import com.example.demo.mvc.MockSpringSecurityFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
	controllers = UserController.class,
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = WebSecurityConfig.class
		)
	}
)
public class UserControllerTest {
	private MockMvc mvc;

	private Principal mockPrincipal;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@MockBean
	JwtUtil jwtUtil;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(springSecurity(new MockSpringSecurityFilter()))
			.build();
	}

	@Test
	@DisplayName("회원가입 테스트")
	void signupTest() throws Exception{
		//given
		SignRequestDto requestDto = new SignRequestDto("testname", "testnick", "1234");
		String requestJson = objectMapper.writeValueAsString(requestDto);
		//when - then
		mvc.perform(post("/auth/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("로그인 테스트")
	void loginTest() throws Exception{
		//given
		LoginRequestDto requestDto = new LoginRequestDto("testname", "testpass");
		LoginResponseDto responseDto = new LoginResponseDto("testToken");
		String requestJson = objectMapper.writeValueAsString(requestDto);
		when(userService.login(any(LoginRequestDto.class))).thenReturn(responseDto);
		//when - then
		mvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
			.andExpect(status().isOk());
	}
}
