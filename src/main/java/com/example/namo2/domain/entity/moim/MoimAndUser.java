package com.example.namo2.domain.entity.moim;


import com.example.namo2.global.common.entity.BaseTimeEntity;
import com.example.namo2.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim_and_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimAndUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_and_user_id")
    private Long id;

    @Column(name="moim_custom_name")
    private String moimCustomName;

    private Integer color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @Builder
    public MoimAndUser(String moimCustomName, Integer color, User user, Moim moim) {
        this.moimCustomName = moimCustomName;
        this.color = color;
        this.user = user;
        this.moim = moim;
    }

    public void updateCustomName(String name) {
        this.moimCustomName = name;
    }
}
