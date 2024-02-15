package com.example.namo2.domain.moim.application.converter;

import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.ui.dto.MoimResponse;

public class MoimResponseConverter {
    private MoimResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static MoimResponse.MoimIdDto toMoimIdDto(Moim moim) {
        return new MoimResponse.MoimIdDto(moim.getId());
    }
}
