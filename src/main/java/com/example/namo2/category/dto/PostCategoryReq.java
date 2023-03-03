package com.example.namo2.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCategoryReq {
    private String name;
    private Long palletId;
    private boolean isShare;
}
