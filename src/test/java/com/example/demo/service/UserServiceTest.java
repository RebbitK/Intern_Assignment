package com.example.demo.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.LoginResponseDto;
import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.dto.SignResponseDto;
import com.example.demo.domain.user.entity.RoleEnum;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user.service.UserServiceImpl;
import com.example.demo.global.exception.customException.DuplicateNicknameException;
import com.example.demo.global.exception.customException.DuplicateUsernameException;
import com.example.demo.global.exception.customException.InvalidCredentialsException;
import com.example.demo.global.util.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	MessageSource messageSource;

	@Mock
	JwtUtil jwtUtil;

	@InjectMocks
	UserServiceImpl userService;

	private User testUser() {
		String userName = "testUser";
		String password = "testPassword";
		String nickName = "testNickName";
		SignRequestDto requestDto = new SignRequestDto(userName, password, nickName);
		return new User(requestDto, password);
	}

	@Test
	@DisplayName("회원가입 테스트")
	void signTest() {
		//given
		SignRequestDto requestDto = new SignRequestDto("testname", "testnick", "1234");
		User user = new User(requestDto, "1234");
		when(userRepository.save(any(User.class))).thenReturn(user);
		//when
		SignResponseDto responseDto = userService.signUp(requestDto);
		//then
		assertEquals(responseDto.getUsername(), requestDto.getUsername());
	}

	@Test
	@DisplayName("로그인 테스트")
	void loginTest() {
		//given
		LoginRequestDto requestDto = new LoginRequestDto("testname", "testPassword");
		String token = "testToken";
		User user = testUser();
		when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(
			Optional.of(user));
		when(passwordEncoder.matches(requestDto.getPassword(),"testPassword")).thenReturn(true);
		when(jwtUtil.createToken(eq(null), any(String.class), any(String.class),
			any(RoleEnum.class))).thenReturn(token);
		//when
		LoginResponseDto responseDto = userService.login(requestDto);
		//then
		assertEquals(responseDto.getToken(), token);
	}

	@Test
	@DisplayName("회원가입 실패 - 중복 아이디")
	void signFailTest_1() {
		//given
		SignRequestDto requestDto = new SignRequestDto("testname", "testnick", "1234");
		User user = new User(requestDto, "1234");
		when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
		//when - then
		Exception exception = assertThrows(DuplicateUsernameException.class, ()-> userService.signUp(requestDto));
	}

	@Test
	@DisplayName("회원가입 실패 - 중복 닉네임")
	void signFailTest_2() {
		//given
		SignRequestDto requestDto = new SignRequestDto("testname", "testnick", "1234");
		User user = new User(requestDto, "1234");
		when(userRepository.findByNickname(requestDto.getNickname())).thenReturn(Optional.of(user));
		//when - then
		Exception exception = assertThrows(DuplicateNicknameException.class, ()-> userService.signUp(requestDto));
	}

	@Test
	@DisplayName("로그인 실패 - 존재하지 않는 아이디")
	void loginFailTest_1(){
		//given
		LoginRequestDto requestDto = new LoginRequestDto("testname", "testPassword");
		//when - then
		Exception exception = assertThrows(InvalidCredentialsException.class, ()-> userService.login(requestDto));
	}

	@Test
	@DisplayName("로그인 실패 - 존재하지 않는 비밀번호")
	void loginFailTest_2(){
		//given
		LoginRequestDto requestDto = new LoginRequestDto("testname", "testPassword");
		User user = testUser();
		when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(
			Optional.of(user));
		//when - then
		Exception exception = assertThrows(InvalidCredentialsException.class, ()-> userService.login(requestDto));
	}

}
