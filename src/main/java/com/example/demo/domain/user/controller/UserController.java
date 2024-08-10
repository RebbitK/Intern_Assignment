package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.LoginResponseDto;
import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.dto.SignResponseDto;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.common.CommonResponse;
import com.example.demo.global.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입", description = "회원가입")
	@PostMapping("/auth/signup")
	public ResponseEntity<CommonResponse<SignResponseDto>> signup(
		@RequestBody SignRequestDto requestDto) {
		SignResponseDto responseDto = userService.signUp(requestDto);
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<SignResponseDto>builder()
				.msg("signup success")
				.data(responseDto)
				.build());
	}

	@Operation(summary = "로그인", description = "로그인")
	@PostMapping("/auth/login")
	public ResponseEntity<CommonResponse<LoginResponseDto>> login(
		@RequestBody LoginRequestDto requestDto,
		HttpServletResponse response) {
		LoginResponseDto responseDto = userService.login(requestDto);
		response.setHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getToken());
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<LoginResponseDto>builder()
				.msg("login success")
				.data(responseDto)
				.build());
	}


}
