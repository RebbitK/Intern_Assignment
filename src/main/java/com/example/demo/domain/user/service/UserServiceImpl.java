package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.dto.SignResponseDto;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.customException.DuplicateNicknameException;
import com.example.demo.global.exception.customException.DuplicateUsernameException;
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

}
