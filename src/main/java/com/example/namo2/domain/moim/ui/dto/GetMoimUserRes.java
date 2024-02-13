package com.example.namo2.domain.moim.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMoimUserRes {
    private Long userId;
    private String userName;
    private Integer color;
}
