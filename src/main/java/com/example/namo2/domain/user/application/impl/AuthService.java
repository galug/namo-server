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
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;


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
