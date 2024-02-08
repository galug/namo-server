package com.example.namo2.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/log")
    @ResponseBody
    public String testLog() {
        return "test";
    }

    @GetMapping("/authenticate")
    @ResponseBody
    public String test() {
        return "인증 완료";
    }

}
