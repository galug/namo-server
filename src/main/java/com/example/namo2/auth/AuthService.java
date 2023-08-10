package com.example.namo2.auth;

import com.example.namo2.category.CategoryRepository;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.entity.category.Category;
import com.example.namo2.entity.user.User;
import com.example.namo2.palette.PaletteRepository;
import com.example.namo2.auth.dto.SignUpReq;
import com.example.namo2.auth.dto.SignUpRes;
import com.example.namo2.auth.dto.SocialSignUpReq;
import com.example.namo2.utils.JwtUtils;
import com.example.namo2.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Optional;

import static com.example.namo2.config.response.BaseResponseStatus.EXPIRATION_REFRESH_TOKEN;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.SOCIAL_LOGIN_FAILURE;

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

    @Transactional(readOnly = false)
    public SignUpRes signupNaver(SocialSignUpReq signUpReq) throws BaseException {
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
            User savedUser = saveOrFind(user);
            SignUpRes signUpRes = jwtUtils.generateTokens(savedUser.getId());
            updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
            return signUpRes;
        } catch (IOException e) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
    }

    @Transactional(readOnly = false)
    public SignUpRes signupKakao(SocialSignUpReq signUpReq) throws BaseException {
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
            User savedUser = saveOrFind(user);
            SignUpRes signUpRes = jwtUtils.generateTokens(savedUser.getId());
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
    public SignUpRes reissueAccessToken(SignUpReq signUpReq) throws BaseException {
        if (!jwtUtils.validateToken(signUpReq.getRefreshToken())) {
            throw new BaseException(EXPIRATION_REFRESH_TOKEN);
        }

        User user = userDao.findUserByRefreshToken(signUpReq.getRefreshToken())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));

        SignUpRes signUpRes = jwtUtils.generateTokens(user.getId());
        user.updateRefreshToken(signUpRes.getRefreshToken());
        return signUpRes;
    }
}