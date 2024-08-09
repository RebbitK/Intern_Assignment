package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.dto.SignResponseDto;

public interface UserService {

	SignResponseDto signUp(SignRequestDto requestDto);

}
