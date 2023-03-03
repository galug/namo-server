package com.example.namo2.user;

import com.example.namo2.config.BaseException;
import com.example.namo2.entity.User;
import com.example.namo2.user.dto.SignUpReq;
import com.example.namo2.user.dto.SignUpRes;
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

import static com.example.namo2.config.BaseResponseStatus.EXPIRATION_REFRESH_TOKEN;
import static com.example.namo2.config.BaseResponseStatus.NOT_FOUND_USER_FAILURE;
import static com.example.namo2.config.BaseResponseStatus.SOCIAL_LOGIN_FAILURE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final SocialUtils socialUtils;

    public SignUpRes signupNaver(SignUpReq signUpReq) throws BaseException {
        try {
            HttpURLConnection con = socialUtils.connectNaverResourceServer(signUpReq);
            socialUtils.validateSocialAccessToken(con);

            String result = socialUtils.findSocialLoginUsersInfo(con);

            Map<String, String> response = socialUtils.findResponseFromNaver(result);
            User user = User.builder()
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .birthDay((String) response.get("birthday"))
                    .build();
            User savedUser = saveOrFind(user);
            SignUpRes signUpRes = jwtUtils.generateTokens(savedUser.getId());
            updateRefreshToken(savedUser.getId(), signUpRes.getRefreshToken());
            return signUpRes;
        } catch (IOException e) {
            throw new BaseException(SOCIAL_LOGIN_FAILURE);
        }
    }

    public SignUpRes signupKakao(SignUpReq signUpReq) throws BaseException {
        try {
            HttpURLConnection con = socialUtils.connectKakaoResourceServer(signUpReq);
            socialUtils.validateSocialAccessToken(con);

            String result = socialUtils.findSocialLoginUsersInfo(con);

            log.debug("result = " + result);

            Map<String, String> response = socialUtils.findResponseFromKakako(result);
            User user = User.builder()
                    .email((String) response.get("email"))
                    .name((String) response.get("nickname"))
                    .birthDay((String) response.getOrDefault("birthday", null))
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
            return save;
        }
        return userByEmail.get();
    }

    public void updateRefreshToken(Long userId, String refreshToken) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        user.updateRefreshToken(refreshToken);
    }

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


//public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//    private final UserDao userDao;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//        // Oauth2 서비스 id 구분 로직(naver kakao 구분)
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        // OAuth2 로그인 진행시 키가 되는 필드 값
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
//                .getUserInfoEndpoint().getUserNameAttributeName();
//
//        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        return oAuthAttribute;
//    }
//
//    public Long saveOrFind(OAuthAttribute attributes) {
//        Optional<User> userByEmail = userDao.findUserByEmail(attributes.getEmail());
//        if (userByEmail.isEmpty()) {
//            User save = userDao.save(attributes.toEntity());
//            return save.getId();
//        }
//        return userByEmail.get().getId();
//    }
//
//    public void updateRefreshToken(Long userId, String refreshToken) throws BaseException {
//        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
//        user.updateRefreshToken(refreshToken);
//    }
//}
