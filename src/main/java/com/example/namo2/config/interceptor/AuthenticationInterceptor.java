package com.example.namo2.config.interceptor;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.user.UserRepository;
import com.example.namo2.utils.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    private final UserRepository userDao;

    @Autowired
    public AuthenticationInterceptor(JwtUtils jwtUtils, UserRepository userDao) {
        this.jwtUtils = jwtUtils;
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Long userId = jwtUtils.resolveRequest(request);
            request.setAttribute("userId", userId);
            return true;
        } catch (BaseException e) {
            Gson gson = new Gson();
            String exception = gson.toJson(new BaseResponse(e.getStatus()));
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(403);
            response.getWriter().print(exception);
            return false;
        }
    }
}
