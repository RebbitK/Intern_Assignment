package com.example.demo.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignRequestDto {

	private String username;
	private String nickname;
	private String password;

}
