package com.example.namo2.domain.moim.domain;

import com.example.namo2.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "moim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Moim extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "moim_id")
    private Long id;

    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    private String code;

    @Builder
    public Moim(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.code = createCode();
    }

    private String createCode() {
        return UUID.randomUUID().toString();
    }
}
