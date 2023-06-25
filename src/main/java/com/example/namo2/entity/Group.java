package com.example.namo2.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "moim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Group {
    @Id
    @GeneratedValue
    @Column(name= "moim_id")
    private Long id;

    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    private String code;

    @Builder
    public Group(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.code = createCode();
    }

    private String createCode() {
        return UUID.randomUUID().toString();
    }
}
