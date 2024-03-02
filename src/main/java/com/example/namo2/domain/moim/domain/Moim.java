package com.example.namo2.domain.moim.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.example.namo2.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moim")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Moim extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moim_id")
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
