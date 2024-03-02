package com.example.namo2.domain.memo.ui.dto;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;

import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;

import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MoimMemoResponse {

	@NoArgsConstructor
	@Getter
	public static class MoimMemoDto {
		private String name;
		private Long startDate;
		private String locationName;
		private List<MoimUserDto> users;
		private List<MoimMemoLocationDto> locationDtos;

		@Builder
		public MoimMemoDto(MoimMemo moimMemo, List<MoimMemoLocationDto> moimMemoLocationDtos) {
			this.name = moimMemo.getMoimSchedule().getName();
			this.startDate = moimMemo.getMoimSchedule().getPeriod().getStartDate().atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.locationName = moimMemo.getMoimSchedule().getLocation().getLocationName();
			this.users = moimMemo.getMoimSchedule().getMoimScheduleAndUsers().stream()
				.map(MoimUserDto::new)
				.collect(Collectors.toList());
			this.locationDtos = moimMemoLocationDtos;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class MoimUserDto {
		private Long userId;
		private String userName;

		public MoimUserDto(MoimScheduleAndUser moimScheduleAndUser) {
			this.userId = moimScheduleAndUser.getUser().getId();
			this.userName = moimScheduleAndUser.getUser().getName();
		}
	}

	@NoArgsConstructor
	@Getter
	public static class MoimMemoLocationDto {
		private Long moimMemoLocationId;
		private String name;
		private Integer money;
		private List<Long> participants;
		private List<String> urls;

		@Builder
		public MoimMemoLocationDto(Long moimMemoLocationId, String name, Integer money, List<String> urls,
			List<MoimMemoLocationAndUser> participants) {
			this.moimMemoLocationId = moimMemoLocationId;
			this.name = name;
			this.money = money;
			this.urls = urls;
			this.participants = participants.stream()
				.map(moimMemoLocationAndUser -> moimMemoLocationAndUser.getUser().getId())
				.collect(Collectors.toList());
		}
	}

	@Getter
	@NoArgsConstructor
	public static class SliceDiaryDto<T> {
		private List<T> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;

		public SliceDiaryDto(Slice<T> slice) {
			this.content = slice.getContent();
			this.currentPage = slice.getNumber();
			this.size = content.size();
			this.first = slice.isFirst();
			this.last = slice.isLast();
		}
	}

	@Getter
	public static class DiaryDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<String> urls;
		private Long categoryId;
		private Long color;
		private String placeName;

		public DiaryDto(Schedule schedule) {
			this.scheduleId = schedule.getId();
			this.name = schedule.getName();
			this.startDate = schedule.getPeriod().getStartDate().atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.contents = schedule.getContents();
			this.categoryId = schedule.getCategory().getId();
			this.color = schedule.getCategory().getPalette().getId();
			this.placeName = schedule.getLocation().getLocationName();
			this.urls = schedule.getImages().stream()
				.map(Image::getImgUrl)
				.collect(Collectors.toList());
		}

		public DiaryDto(MoimScheduleAndUser moimScheduleAndUser) {
			this.scheduleId = moimScheduleAndUser.getMoimSchedule().getId();
			this.name = moimScheduleAndUser.getMoimSchedule().getName();
			this.startDate = moimScheduleAndUser.getMoimSchedule()
				.getPeriod()
				.getStartDate()
				.atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.contents = moimScheduleAndUser.getMemo();
			this.categoryId = moimScheduleAndUser.getCategory().getId();
			this.color = moimScheduleAndUser.getCategory().getPalette().getId();
			this.placeName = moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName();
			this.urls = moimScheduleAndUser.getMoimSchedule().getMoimMemo()
				.getMoimMemoLocations()
				.stream()
				.flatMap(location -> location
					.getMoimMemoLocationImgs()
					.stream())
				.map(MoimMemoLocationImg::getUrl)
				.limit(3)
				.collect(Collectors.toList());
		}
	}
}
