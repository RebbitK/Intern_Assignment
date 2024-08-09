package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.RoleEnum;
import com.example.demo.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignResponseDto {

	private String username;
	private String nickname;
	private RoleEnum role;

	public SignResponseDto(User user){
		this.username = user.getUsername();
		this.nickname = user.getNickname();
		this.role = user.getRole();
	}
}
