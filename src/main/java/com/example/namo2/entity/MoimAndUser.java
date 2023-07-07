package com.example.namo2.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "moim_and_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimAndUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_and_user_id")
    private Long id;

    @Column(name="moim_custom_name")
    private String moimCustomName;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @Builder
    public MoimAndUser(String moimCustomName, String color, User user, Moim moim) {
        this.moimCustomName = moimCustomName;
        this.color = color;
        this.user = user;
        this.moim = moim;
    }

    public void updateCustomName(String name) {
        this.moimCustomName = name;
    }
}
