package com.example.namo2.domain.user.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.namo2.domain.category.dao.repository.CategoryRepository;
import com.example.namo2.domain.category.dao.repository.PaletteRepository;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.ui.dto.UserRequest;
import com.example.namo2.domain.user.ui.dto.UserResponse;
import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.JwtUtils;
import com.example.namo2.global.utils.SocialUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userDao;
    private final PaletteRepository paletteRepository;
    private final CategoryRepository categoryRepository;
    private final JwtUtils jwtUtils;
    private final SocialUtils socialUtils;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = false)
    public UserResponse.SignUpDto signupNaver(UserRequest.SocialSignUpDto signUpDto) throws BaseException {
        try {
            HttpURLConnection con = socialUtils.connectNaverResourceServer(signUpDto);
            socialUtils.validateSocialAccessToken(con);

            String result = socialUtils.findSocialLoginUsersInfo(con);

            Map<String, String> response = socialUtils.findResponseFromNaver(result);
            User user = User.builder()
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .birthday((String) response.get("birthday"))
                    .build();
            User savedUser = saveOrFind(user);
            UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
            updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
            return signUpRes;
        } catch (IOException e) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
    }

    @Transactional(readOnly = false)
    public UserResponse.SignUpDto signupKakao(UserRequest.SocialSignUpDto signUpDto) throws BaseException {
        try {
            HttpURLConnection con = socialUtils.connectKakaoResourceServer(signUpDto);
            socialUtils.validateSocialAccessToken(con);

            String result = socialUtils.findSocialLoginUsersInfo(con);

            log.debug("result = " + result);

            Map<String, String> response = socialUtils.findResponseFromKakako(result);
            User user = User.builder()
                    .email((String) response.get("email"))
                    .name((String) response.get("nickname"))
                    .birthday((String) response.getOrDefault("birthday", null))
                    .build();
            User savedUser = saveOrFind(user);
            UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(savedUser.getId());
            updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
            return signUpRes;
        } catch (IOException e) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
    }

    public User saveOrFind(User user) {
        Optional<User> userByEmail = userDao.findUserByEmail(user.getEmail());
        if (userByEmail.isEmpty()) {
            User save = userDao.save(user);
            makeBaseCategory(save);
            return save;
        }
        return userByEmail.get();
    }

    private void makeBaseCategory(User save) {
        Category baseCategory = Category.builder()
                .name("기본")
                .palette(paletteRepository.getReferenceById(1L))
                .share(Boolean.TRUE)
                .user(save)
                .build();
        Category groupCategory = Category.builder()
                .name("모임")
                .palette(paletteRepository.getReferenceById(4L))
                .share(Boolean.TRUE)
                .user(save)
                .build();
        categoryRepository.save(baseCategory);
        categoryRepository.save(groupCategory);
    }

    public void updateRefreshToken(Long userId, String refreshToken) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        user.updateRefreshToken(refreshToken);
    }

    @Transactional(readOnly = false)
    public UserResponse.SignUpDto reissueAccessToken(UserRequest.SignUpDto signUpDto) throws BaseException {
        if (!jwtUtils.validateToken(signUpDto.getRefreshToken())) {
            throw new BaseException(EXPIRATION_REFRESH_TOKEN);
        }
        validateLogout(signUpDto);

        User user = userDao.findUserByRefreshToken(signUpDto.getRefreshToken())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        UserResponse.SignUpDto signUpRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(signUpRes.getRefreshToken());
        return signUpRes;
    }

    private void validateLogout(UserRequest.SignUpDto signUpDto) {
        String blackToken = redisTemplate.opsForValue().get(signUpDto.getAccessToken());
        if (StringUtils.hasText(blackToken)) {
            throw new BaseException(BaseResponseStatus.LOGOUT_ERROR);
        }
    }

    public void logout(UserRequest.LogoutDto logoutDto) {
        // AccessToken 만료시 종료
        if (!jwtUtils.validateToken(logoutDto.getAccessToken())) {
            throw new BaseException(EXPIRATION_REFRESH_TOKEN);
        }

        Long expiration = jwtUtils.getExpiration(logoutDto.getAccessToken());
        redisTemplate.opsForValue().set(logoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }
}
