package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.LoginResponseDto;
import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.dto.SignResponseDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.customException.DuplicateNicknameException;
import com.example.demo.global.exception.customException.DuplicateUsernameException;
import com.example.demo.global.exception.customException.InvalidCredentialsException;
import com.example.demo.global.util.JwtUtil;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageSource messageSource;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public SignResponseDto signUp(SignRequestDto requestDto){
		if(userRepository.findByUsername(requestDto.getUsername()).isPresent()){
			throw new DuplicateUsernameException(
				messageSource.getMessage("duplicate.username", null, Locale.KOREA));

		}
		if(userRepository.findByNickname(requestDto.getNickname()).isPresent()){
			throw new DuplicateNicknameException(
				messageSource.getMessage("duplicate.nickname", null, Locale.KOREA));
		}
		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
		User user = new User(requestDto, encodedPassword);
		User savedUser = userRepository.save(user);
		return new SignResponseDto(savedUser);
	}

	@Override
	public LoginResponseDto login(LoginRequestDto requestDto){
		User findUser = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
			() -> new InvalidCredentialsException(
				messageSource.getMessage("invalid.credentials.username", null, Locale.KOREA))
		);
		if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
			throw new InvalidCredentialsException(
				messageSource.getMessage("invalid.credentials.password", null, Locale.KOREA));
		}
		String token = jwtUtil.createToken(findUser.getId(),findUser.getUsername(),findUser.getNickname(),findUser.getRole());
		return new LoginResponseDto(token);
	}

}
