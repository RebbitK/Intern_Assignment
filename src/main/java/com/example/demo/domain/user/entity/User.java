package com.example.demo.domain.user.entity;

import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Enumerated(EnumType.STRING)
	private RoleEnum role;

	public User(Long id, String nickname,String authority)
	{
		this.id = id;
		this.nickname = nickname;
		this.role = RoleEnum.valueOf(authority);
	}

	public User(SignRequestDto requestDto,String password){
		this.username = requestDto.getUsername();
		this.password = password;
		this.nickname = requestDto.getNickname();
		this.role = RoleEnum.USER;
	}
}