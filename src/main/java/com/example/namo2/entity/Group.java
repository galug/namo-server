package com.example.namo2.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id
    @GeneratedValue
    @Column(name= "group_id")
    private Long id;

    private String name;

    private String groupImgUrl;

    private String code;
}
