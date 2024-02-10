package com.example.namo2.domain.category.application.impl;

import com.example.namo2.domain.category.dao.repository.PaletteRepository;
import com.example.namo2.domain.category.domain.Palette;
import com.example.namo2.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.namo2.global.common.response.BaseResponseStatus.NOT_FOUND_PALETTE_FAILURE;

@Service
@RequiredArgsConstructor
public class PaletteService {

    private final PaletteRepository paletteRepository;

    public Palette getPalette(Long paletteId) {
        return paletteRepository.findById(paletteId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
    }
}
