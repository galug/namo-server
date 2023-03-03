package com.example.namo2.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long categoryId;
    private String name;
    private Long paletteId;
    private Boolean isShare;
}
