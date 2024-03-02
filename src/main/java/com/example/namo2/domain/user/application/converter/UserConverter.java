package com.example.namo2.domain.user.application.converter;

import java.util.Map;

import com.example.namo2.domain.user.domain.User;

public class UserConverter {

	private UserConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static User toUser(Map<String, String> response) {
		return User.builder()
			.email(response.get("email"))
			.name(response.get("nickname"))
			.birthday(response.getOrDefault("birthday", null))
			.build();
	}

	public static User toUser(String email, String name) {
		return User.builder()
			.email(email)
			.name(name)
			.build();
	}

}
