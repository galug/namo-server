package com.example.namo2.domain.moim.application.converter;

import com.example.namo2.domain.moim.domain.Moim;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.user.domain.User;

public class MoimAndUserConverter {
    private MoimAndUserConverter() {
        throw new IllegalArgumentException("Util Class");
    }

    public static MoimAndUser toMoimAndUser(String moimCustomName, Integer color, User user, Moim moim) {
        return MoimAndUser.builder()
                .moimCustomName(moimCustomName)
                .color(color)
                .user(user)
                .moim(moim)
                .build();
    }
}
