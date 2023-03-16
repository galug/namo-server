package com.example.namo2.config.interceptor;

import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.user.UserDao;
import com.example.namo2.utils.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.namo2.config.response.BaseResponseStatus.INVALID_TOKEN;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    private final UserDao userDao;

    @Autowired
    public AuthenticationInterceptor(JwtUtils jwtUtils, UserDao userDao) {
        this.jwtUtils = jwtUtils;
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!jwtUtils.validateRequest(request)) {
            Gson gson = new Gson();
            String exception = gson.toJson(new BaseResponse(INVALID_TOKEN));
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(exception);
            return false;
        }
        return true;
    }
}
