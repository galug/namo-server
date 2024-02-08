package com.example.namo2.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCategoryReq {
    private String name;
    private Long paletteId;
    private boolean isShare;
}
