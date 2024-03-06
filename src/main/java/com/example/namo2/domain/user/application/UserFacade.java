package com.example.namo2.domain.user.application;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.namo2.domain.category.application.converter.CategoryConverter;
import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.application.impl.PaletteService;
import com.example.namo2.domain.category.domain.Category;

import com.example.namo2.domain.user.application.converter.TermConverter;
import com.example.namo2.domain.user.application.converter.UserConverter;
import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.Term;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.ui.dto.UserRequest;
import com.example.namo2.domain.user.ui.dto.UserResponse;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.feignClient.apple.AppleAuthClient;
import com.example.namo2.global.feignClient.apple.AppleResponse;
import com.example.namo2.global.feignClient.apple.AppleResponseConverter;
import com.example.namo2.global.feignClient.kakao.KakaoAuthClient;
import com.example.namo2.global.utils.JwtUtils;
import com.example.namo2.global.utils.SocialUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserFacade {
	private final SocialUtils socialUtils;
	private final JwtUtils jwtUtils;
	private final RedisTemplate<String, String> redisTemplate;

	private final UserService userService;
	private final PaletteService paletteService;
	private final CategoryService categoryService;

	private final KakaoAuthClient kakaoAuthClient;
	private final AppleAuthClient appleAuthClient;

	@Value("${spring.security.oauth1.client.registration.apple.client-id}")
	private String clientId;

	@Transactional
	public UserResponse.SignUpDto signupKakao(UserRequest.SocialSignUpDto signUpDto) {
		try {
			HttpURLConnection con = socialUtils.connectKakaoResourceServer(signUpDto);
			socialUtils.validateSocialAccessToken(con);

			String result = socialUtils.findSocialLoginUsersInfo(con);

			log.debug("result = " + result);

			Map<String, String> response = socialUtils.findResponseFromKakako(result);
			User user = UserConverter.toUser(response);
			User savedUser = saveOrNot(user);
			UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
			userService.updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
			return signUpRes;
		} catch (IOException e) {
			throw new BaseException(SOCIAL_LOGIN_FAILURE);
		}
	}

	@Transactional
	public UserResponse.SignUpDto signupNaver(UserRequest.SocialSignUpDto signUpDto) {
		try {
			HttpURLConnection con = socialUtils.connectNaverResourceServer(signUpDto);
			socialUtils.validateSocialAccessToken(con);

			String result = socialUtils.findSocialLoginUsersInfo(con);

			Map<String, String> response = socialUtils.findResponseFromNaver(result);
			User user = UserConverter.toUser(response);
			User savedUser = saveOrNot(user);
			UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
			userService.updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
			return signUpRes;
		} catch (IOException e) {
			throw new BaseException(SOCIAL_LOGIN_FAILURE);
		}
	}

	@Transactional
	public UserResponse.SignUpDto signupApple(UserRequest.AppleSignUpDto req) {
		AppleResponse.ApplePublicKeyListDto applePublicKeys = appleAuthClient.getApplePublicKeys();
		AppleResponse.ApplePublicKeyDto applePublicKey = null;

		try {
			JSONParser parser = new JSONParser();
			String[] decodeArr = req.getIdentityToken().split("\\.");
			String header = new String(Base64.getDecoder().decode(decodeArr[0]));
			JSONObject headerJson = (JSONObject)parser.parse(header);

			Object kid = headerJson.get("kid"); //개발자 계정에서 얻은 10자리 식별자 키
			Object alg = headerJson.get("alg"); //토큰을 암호화하는데 사용되는 암호화 알고리즘

			//identityToken 검증
			applePublicKey = AppleResponseConverter.toApplePublicKey(applePublicKeys, kid, alg);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		PublicKey publicKey = getPublicKey(applePublicKey);
		validateToken(publicKey, req.getIdentityToken());

		Claims claims = Jwts.parserBuilder()
			.setSigningKey(publicKey)
			.build()
			.parseClaimsJws(req.getIdentityToken())
			.getBody();
		String appleOauthId = claims.get("sub", String.class);
		String appleEmail = claims.get("email", String.class);
		log.debug("email: {}, oauthId : {}", appleEmail, appleOauthId);

		User user = UserConverter.toUser(req.getEmail(), req.getUsername());
		User savedUser = saveOrNot(user);
		UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
		userService.updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
		return signUpRes;
	}

	private PublicKey getPublicKey(AppleResponse.ApplePublicKeyDto applePublicKey) {
		String nStr = applePublicKey.getN(); //RSA public key의 모듈러스 값
		String eStr = applePublicKey.getE(); //RSA public key의 지수 값

		byte[] nBytes = Base64.getUrlDecoder().decode(nStr);
		byte[] eBytes = Base64.getUrlDecoder().decode(eStr);

		BigInteger n = new BigInteger(1, nBytes);
		BigInteger e = new BigInteger(1, eBytes);

		try {
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
			KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
			return keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new BaseException(MAKE_PUBLIC_KEY_FAILURE);
		}

	}

	private boolean validateToken(PublicKey publicKey, String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();

		String issuer = (String)claims.get("iss");
		if (!"https://appleid.apple.com".equals(issuer)) {
			throw new IllegalArgumentException("Invalid issuer");
		}

		String audience = (String)claims.get("aud");
		log.debug("{}", audience);
		if (!clientId.equals(audience)) {
			throw new IllegalArgumentException("Invalid audience");
		}

		long expiration = claims.getExpiration().getTime();
		log.debug("expriation : {} < now : {}", expiration, (new Date()).getTime());
		if (expiration <= (new Date()).getTime()) {
			throw new IllegalArgumentException("Token expired");
		}
		return true;
	}

	@Transactional
	public UserResponse.SignUpDto reissueAccessToken(UserRequest.SignUpDto signUpDto) {
		if (!jwtUtils.validateToken(signUpDto.getRefreshToken())) {
			throw new BaseException(EXPIRATION_REFRESH_TOKEN);
		}
		validateLogout(signUpDto);

		User user = userService.getUserByRefreshToken(signUpDto.getRefreshToken());
		UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(user.getId());
		user.updateRefreshToken(signUpRes.getRefreshToken());
		return signUpRes;
	}

	@Transactional
	public void logout(UserRequest.LogoutDto logoutDto) {
		// AccessToken 만료시 종료
		if (!jwtUtils.validateToken(logoutDto.getAccessToken())) {
			throw new BaseException(EXPIRATION_REFRESH_TOKEN);
		}

		Long expiration = jwtUtils.getExpiration(logoutDto.getAccessToken());
		redisTemplate.opsForValue().set(logoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
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
		Category baseCategory = CategoryConverter.toCategory(
			"일정",
			paletteService.getReferenceById(1L),
			Boolean.TRUE,
			save
		);
		Category groupCategory = CategoryConverter.toCategory(
			"모임",
			paletteService.getReferenceById(4L),
			Boolean.TRUE,
			save
		);

		categoryService.create(baseCategory);
		categoryService.create(groupCategory);
	}

	private void validateLogout(UserRequest.SignUpDto signUpDto) {
		String blackToken = redisTemplate.opsForValue().get(signUpDto.getAccessToken());
		if (StringUtils.hasText(blackToken)) {
			throw new BaseException(BaseResponseStatus.LOGOUT_ERROR);
		}
	}


	@Transactional(readOnly = false)
	public void createTerm(UserRequest.TermDto termDto, Long userId) {
		User user = userService.getUser(userId);
		List<Term> terms = TermConverter.toTerms(termDto, user);
		userService.createTerm(terms);
	}

	@Transactional
	public void removeKakaoUser(HttpServletRequest request, String kakaoAccessToken){
		kakaoAuthClient.unlinkKakao(kakaoAccessToken);

		removeUserFromDB(request);
	}

	private void removeUserFromDB(HttpServletRequest request){
		String accessToken = request.getHeader("Authorization");
		//유저 토큰 만료시 예외 처리
		if (!jwtUtils.validateToken(accessToken)) {
			throw new BaseException(EXPIRATION_REFRESH_TOKEN);
		}
		Long expiration = jwtUtils.getExpiration(accessToken);
		redisTemplate.opsForValue().set(accessToken, "delete", expiration, TimeUnit.MILLISECONDS);

		//db에서 삭제
		User user = userService.getUser(jwtUtils.resolveRequest(request));
		userService.removeUser(user);
	}
}
