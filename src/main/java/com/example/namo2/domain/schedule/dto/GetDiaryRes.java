package com.example.namo2.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetDiaryRes {
    private String texts;
    private List<String> urls;
}
