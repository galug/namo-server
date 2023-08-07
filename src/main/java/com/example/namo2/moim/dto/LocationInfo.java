package com.example.namo2.moim.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class LocationInfo {
    private String name;
    private Integer money;
    private List<Long> participants;
}
