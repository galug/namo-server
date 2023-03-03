package com.example.namo2.utils;

import com.example.namo2.config.BaseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    JwtUtils jwtUtils;

    @Test
    public void resolveToken() {
//        try {
////            String accessToken = jwtUtils.createAccessToken();
////            System.out.println("accessToken = " + accessToken);
////            Long id = jwtUtils.resolveToken(accessToken);
////            Assertions.assertThat(jwtUtils.resolveToken(accessToken)).isEqualTo(1L);
//        } catch (BaseException e) {
//            e.printStackTrace();
//        }
    }
}