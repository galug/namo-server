package com.example.namo2.entity;


import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "moim_and_user")
public class GroupAndUser {
    @Id
    @GeneratedValue
    @Column(name = "moim_and_user_id")
    private Long id;

    @Column(name="moim_custom_name")
    private String groupCustomName;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Group group;

    @Builder
    public GroupAndUser(String groupCustomName, String color, User user, Group group) {
        this.groupCustomName = groupCustomName;
        this.color = color;
        this.user = user;
        this.group = group;
    }
}
