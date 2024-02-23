package com.example.namo2.domain.memo.domain;

import com.example.namo2.domain.user.domain.User;
import com.example.namo2.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_memo_location_and_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MoimMemoLocationAndUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_memo_location_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_memo_location_id")
    private MoimMemoLocation moimMemoLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public MoimMemoLocationAndUser(Long id, MoimMemoLocation moimMemoLocation, User user) {
        this.id = id;
        this.moimMemoLocation = moimMemoLocation;
        this.user = user;
        this.moimMemoLocation.getMoimMemoLocationAndUsers().add(this);
    }
}
