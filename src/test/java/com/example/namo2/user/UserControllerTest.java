package com.example.namo2.user;

import com.example.namo2.user.dto.SignUpReq;
import com.example.namo2.user.dto.SignUpRes;
import com.example.namo2.user.dto.SocialSignUpReq;
import com.example.namo2.utils.JwtUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("카카오 로그인 성공 로직")
    void kakaoSignup() throws Exception {
        // given
        SignUpRes signUpRes = new SignUpRes("accessToken", "refreshToken");
        SocialSignUpReq socialAccessToken = new SocialSignUpReq("socialAccessToken");
        given(userService.signupKakao(socialAccessToken))
                .willReturn(signUpRes);
        Gson gson = new Gson();
        String jsonToken = gson.toJson(socialAccessToken);

        // when
        ResultActions perform = mvc.perform(post("/auth/kakao/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToken));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.result.refreshToken").value("refreshToken"))
                .andDo(print());
    }

    @Test
    @DisplayName("네이버 로그인 성공 로직")
    void naverSignup() throws Exception {
        // given
        SignUpRes signUpRes = new SignUpRes("accessToken", "refreshToken");
        SocialSignUpReq socialAccessToken = new SocialSignUpReq("socialAccessToken");
        given(userService.signupNaver(socialAccessToken))
                .willReturn(signUpRes);
        Gson gson = new Gson();
        String jsonToken = gson.toJson(socialAccessToken);

        // when
        ResultActions perform = mvc.perform(post("/auth/naver/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToken));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.result.refreshToken").value("refreshToken"))
                .andDo(print());
    }

    @Test
    void reissueAccessToken() {
    }
}