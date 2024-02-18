package com.example.namo2.domain.user.application;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.application.impl.PaletteService;
import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.dao.repository.PaletteRepository;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.user.application.converter.UserResponseConverter;
import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.ui.dto.UserRequest;
import com.example.namo2.domain.user.ui.dto.UserResponse;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.utils.JwtUtils;
import com.example.namo2.global.utils.SocialUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserFacade {
	private final SocialUtils socialUtils;
	private final JwtUtils jwtUtils;
	private final UserService userService;
	private final PaletteService paletteService;
	private final CategoryService categoryService;

	public UserResponse.SignUpDto signupKakao(UserRequest.SocialSignUpDto signUpReq
	) throws BaseException {
		try {
			HttpURLConnection con = socialUtils.connectKakaoResourceServer(signUpReq);
			socialUtils.validateSocialAccessToken(con);

			String result = socialUtils.findSocialLoginUsersInfo(con);

			log.debug("result = " + result);

			Map<String, String> response = socialUtils.findResponseFromKakako(result);
			User user = User.builder()
				.email((String) response.get("email"))
				.name((String) response.get("nickname"))
				.birthday((String) response.getOrDefault("birthday", null))
				.build();
			User savedUser = saveOrNot(user);
			UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
			userService.updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
			return signUpRes;
		} catch (IOException e) {
			throw new BaseException(SOCIAL_LOGIN_FAILURE);
		}
	}

	public UserResponse.SignUpDto signupNaver(UserRequest.SocialSignUpDto signUpReq
	) throws BaseException {
		try {
			HttpURLConnection con = socialUtils.connectNaverResourceServer(signUpReq);
			socialUtils.validateSocialAccessToken(con);

			String result = socialUtils.findSocialLoginUsersInfo(con);

			Map<String, String> response = socialUtils.findResponseFromNaver(result);
			User user = User.builder()
				.email((String) response.get("email"))
				.name((String) response.get("name"))
				.birthday((String) response.get("birthday"))
				.build();
			User savedUser = saveOrNot(user);
			UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
			userService.updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
			return signUpRes;
		} catch (IOException e) {
			throw new BaseException(SOCIAL_LOGIN_FAILURE);
		}
	}

	private User saveOrNot(User user) {
		Optional<User> userByEmail = userService.getUserByEmail(user.getEmail());
		if (userByEmail.isEmpty()) {
			User save = userService.createUser(user);
			makeBaseCategory(save);
			return save;
		}
		return userByEmail.get();
	}

	private void makeBaseCategory(User save) {
		Category baseCategory = Category.builder()
			.name("기본")
			.palette(paletteService.getReferenceById(1L))
			.share(Boolean.TRUE)
			.user(save)
			.build();
		Category groupCategory = Category.builder()
			.name("모임")
			.palette(paletteService.getReferenceById(4L))
			.share(Boolean.TRUE)
			.user(save)
			.build();
		categoryService.create(baseCategory);
		categoryService.create(groupCategory);
	}


}
