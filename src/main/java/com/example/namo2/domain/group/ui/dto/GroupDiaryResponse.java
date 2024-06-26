package com.example.namo2.domain.group.ui.dto;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import com.example.namo2.domain.group.domain.MoimMemo;
import com.example.namo2.domain.group.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.group.domain.MoimMemoLocationImg;
import com.example.namo2.domain.group.domain.MoimScheduleAndUser;

import com.example.namo2.domain.individual.domain.Image;
import com.example.namo2.domain.individual.domain.Schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupDiaryResponse {

	private GroupDiaryResponse() {
		throw new IllegalStateException("Utill Classes");
	}

	@NoArgsConstructor
	@Getter
	public static class MoimDiaryDto {
		private String name;
		private Long startDate;
		private String locationName;
		private List<GroupUserDto> users;
		private List<MoimActivityDto> moimActivityDtos;

		@Builder
		public MoimDiaryDto(MoimMemo moimMemo, List<MoimActivityDto> moimActivityDtos) {
			this.name = moimMemo.getMoimSchedule().getName();
			this.startDate = moimMemo
				.getMoimSchedule()
				.getPeriod()
				.getStartDate().atZone(ZoneId.systemDefault())
				.toInstant().getEpochSecond();
			this.locationName = moimMemo.getMoimSchedule().getLocation().getLocationName();
			this.users = moimMemo.getMoimSchedule().getMoimScheduleAndUsers().stream()
				.map(GroupUserDto::new)
				.toList();
			this.moimActivityDtos = moimActivityDtos;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class GroupUserDto {
		private Long userId;
		private String userName;

		public GroupUserDto(MoimScheduleAndUser moimScheduleAndUser) {
			this.userId = moimScheduleAndUser.getUser().getId();
			this.userName = moimScheduleAndUser.getUser().getName();
		}
	}

	@Getter
	@NoArgsConstructor
	public static class MoimActivityDto {
		private Long moimActivityId;
		private String name;
		private Integer money;
		private List<Long> participants;
		private List<String> urls;

		@Builder
		public MoimActivityDto(
			Long moimActivityId,
			String name,
			Integer money,
			List<String> urls,
			List<MoimMemoLocationAndUser> participants
		) {
			this.moimActivityId = moimActivityId;
			this.name = name;
			this.money = money;
			this.urls = urls;
			this.participants = participants.stream()
				.map(moimMemoLocationAndUser -> moimMemoLocationAndUser.getUser().getId())
				.toList();
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
	@NoArgsConstructor
	public static class DiaryDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<String> urls;
		private Long categoryId;
		private Long color;
		private String placeName;

		@Builder
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
				.toList();
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
