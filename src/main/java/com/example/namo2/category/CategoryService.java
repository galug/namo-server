package com.example.namo2.category;

import com.example.namo2.category.dto.CategoryIdRes;
import com.example.namo2.category.dto.PostCategoryReq;
import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponseStatus;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Palette;
import com.example.namo2.palette.PaletteDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.namo2.config.BaseResponseStatus.JPA_FAILURE;
import static com.example.namo2.config.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.config.BaseResponseStatus.NOT_FOUND_PALETTE_FAILURE;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryDao categoryDao;
    private final PaletteDao paletteDao;

    public CategoryIdRes create(PostCategoryReq postcategoryReq) throws BaseException {
        Palette palette = paletteDao.findById(postcategoryReq.getPalletId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        try {
            Category category = Category.builder()
                    .name(postcategoryReq.getName())
                    .palette(palette)
                    .share(postcategoryReq.isShare())
                    .build();
            Category savedCategory = categoryDao.save(category);
            return new CategoryIdRes(savedCategory.getId());
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }

    public CategoryIdRes update(Long categoryId, PostCategoryReq postcategoryReq) throws BaseException {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        Palette palette = paletteDao.findById(postcategoryReq.getPalletId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        try {
            category.update(postcategoryReq.getName(), postcategoryReq.isShare(), palette);
        } catch (Exception exception) {
            throw new BaseException(JPA_FAILURE);
        }
    }
}
