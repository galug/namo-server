//package com.example.namo2.user.dto;
//
//import com.example.namo2.entity.User;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import java.util.Map;
//
//@Slf4j
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Getter
//public class OAuthAttribute implements OAuth2User {
//    private Map<String, Object> attributes;
//    private String nameAttributeKey;
//    private String name;
//    private String email;
//    private String birthDay;
//
//    public static OAuthAttribute of(String registrationId,
//                                    String userNameAttributeName,
//                                    Map<String, Object> attributes) {
//        if (registrationId.equals("naver")) {
//            return ofNaver("id", attributes);
//        }
//        if (registrationId.equals("kakao")) {
//            return ofKakao("id", attributes);
//        }
//        return null;
//    }
//
//    private static OAuthAttribute ofNaver(String userAttributeName, Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//        return OAuthAttribute.builder()
//                .name((String) response.get("name"))
//                .email((String) response.get("email"))
//                .birthDay((String) response.get("birthday"))
//                .attributes(response)
//                .nameAttributeKey(userAttributeName)
//                .build();
//    }
//
//    private static OAuthAttribute ofKakao(String id, Map<String, Object> attributes) {
//        return null;
//    }
//
//    public User toEntity() {
//        return User.builder()
//                .name(name)
//                .email(email)
//                .birthDay(birthDay)
//                .build();
//    }
//
//    @Override
//    public <A> A getAttribute(String name) {
//        return (A) getNameAttributeKey();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//}
