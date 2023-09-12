package com.example.namo2.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
public class LogoutReq {
    @NotBlank
    private String accessToken;
}
