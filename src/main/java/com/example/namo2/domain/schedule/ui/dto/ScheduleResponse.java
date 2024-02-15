package com.example.namo2.domain.schedule.ui.dto;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Alarm;
import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class ScheduleResponse {
	@Getter
	@AllArgsConstructor
	public static class ScheduleIdRes {
		private Long scheduleIdx;
	}

	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class GetScheduleRes {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private Long endDate;
		private List<Integer> alarmDate;
		private Integer interval;
		private Double x;
		private Double y;
		private String locationName;
		private Long categoryId;
		private boolean hasDiary;
		private boolean isMoimSchedule;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GetDiaryByUserRes {
		private Long scheduleId;
		private String contents;
		private List<String> urls;
	}

	@AllArgsConstructor
	@Getter
	public static class GetDiaryByScheduleRes {
		private String contents;
		private List<String> urls;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class SliceDiaryDto {
		private List<DiaryDto> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;
	}
	@Getter
	@AllArgsConstructor
	@Builder
	public static class DiaryDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<String> urls;
		private Long categoryId;
		private Long color;
		private String placeName;
	}
}
