package com.example.demo.domain.user.entity;

public enum RoleEnum {
	USER(Authority.USER),
	ADMIN(Authority.ADMIN);

	private final String authority;

	RoleEnum(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}

	public static class Authority {

		public static final String USER = "ROLE_USER";
		public static final String ADMIN = "ROLE_ADMIN";
	}
}