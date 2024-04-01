package com.example.namo2.domain.schedule.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.domain.Category;

import com.example.namo2.domain.moim.application.impl.MoimScheduleAndUserService;
import com.example.namo2.domain.moim.application.impl.MoimScheduleService;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;

import com.example.namo2.domain.schedule.application.converter.AlarmConverter;
import com.example.namo2.domain.schedule.application.converter.ImageConverter;
import com.example.namo2.domain.schedule.application.converter.ScheduleConverter;
import com.example.namo2.domain.schedule.application.converter.ScheduleResponseConverter;
import com.example.namo2.domain.schedule.application.impl.AlarmService;
import com.example.namo2.domain.schedule.application.impl.ImageService;
import com.example.namo2.domain.schedule.application.impl.ScheduleService;
import com.example.namo2.domain.schedule.domain.Alarm;
import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Period;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;

import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {
	private final UserService userService;
	private final ScheduleService scheduleService;
	private final AlarmService alarmService;
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final MoimScheduleService moimScheduleService;
	private final MoimScheduleAndUserService moimScheduleAndUserService;
	private final FileUtils fileUtils;

	@Transactional
	public ScheduleResponse.ScheduleIdDto createSchedule(ScheduleRequest.PostScheduleDto req, Long userId)
		throws BaseException {

		User user = userService.getUser(userId);
		Category category = categoryService.getCategory(req.getCategoryId());
		Period period = ScheduleConverter.toPeriod(req);
		Schedule schedule = ScheduleConverter.toSchedule(req, period, user, category);
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		schedule.addAlarms(alarms);
		Schedule saveSchedule = scheduleService.createSchedule(schedule);

		return ScheduleResponseConverter.toScheduleIdRes(saveSchedule);
	}

	@Transactional
	public ScheduleResponse.ScheduleIdDto createDiary(Long scheduleId, String content, List<MultipartFile> imgs)
		throws BaseException {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.updateDiaryContents(content);
		if (imgs != null) {
			List<String> urls = fileUtils.uploadImages(imgs);
			List<Image> imgList = urls.stream().map(url -> ImageConverter.toImage(url, schedule)).toList();
			imageService.createImgs(imgList);
		}
		return ScheduleResponseConverter.toScheduleIdRes(schedule);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getSchedulesByUser(Long userId,
		List<LocalDateTime> localDateTimes) throws BaseException {
		User user = userService.getUser(userId);
		return scheduleService.getSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getMoimSchedulesByUser(Long userId,
		List<LocalDateTime> localDateTimes) throws BaseException {
		User user = userService.getUser(userId);
		return scheduleService.getMoimSchedulesByUser(user, localDateTimes.get(0), localDateTimes.get(1));
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getAllSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllSchedulesByUser(user);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getAllMoimSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllMoimSchedulesByUser(user);
	}

	public ScheduleResponse.SliceDiaryDto getMonthDiary(
		Long userId,
		List<LocalDateTime> localDateTimes,
		Pageable pageable
	) throws BaseException {
		User user = userService.getUser(userId);
		return scheduleService.getScheduleDiaryByUser(user, localDateTimes.get(0), localDateTimes.get(1), pageable);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetDiaryByUserDto> getAllDiariesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllDiariesByUser(user);
	}

	@Transactional(readOnly = true)
	public ScheduleResponse.GetDiaryByScheduleDto getDiaryBySchedule(Long scheduleId) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.existDairy(); //다이어리 없으면 exception발생
		List<String> imgUrls = schedule.getImages().stream()
			.map(Image::getImgUrl)
			.collect(Collectors.toList());

		return ScheduleResponseConverter.toGetDiaryByScheduleRes(schedule, imgUrls);
	}

	@Transactional
	public ScheduleResponse.ScheduleIdDto modifySchedule(
		Long scheduleId,
		ScheduleRequest.PostScheduleDto req
	) throws BaseException {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		Category category = categoryService.getCategory(req.getCategoryId());
		Period period = ScheduleConverter.toPeriod(req);

		schedule.clearAlarm();
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		schedule.addAlarms(alarms);

		schedule.updateSchedule(
			req.getName(),
			period,
			category,
			req.getX(),
			req.getY(),
			req.getLocationName()
		);

		return ScheduleResponseConverter.toScheduleIdRes(schedule);
	}

	@Transactional
	public void removeDiary(Long scheduleId) throws BaseException {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.deleteDiary();
		List<String> urls = schedule.getImages().stream()
			.map(Image::getImgUrl)
			.collect(Collectors.toList());
		imageService.removeImgsBySchedule(schedule);
		fileUtils.deleteImages(urls);
	}

	@Transactional
	public void removeSchedule(Long scheduleId, Integer kind, Long userId
	) throws BaseException {
		if (kind == 0) { // 개인 스케줄 :스케줄 알람, 이미지 함께 삭제
			Schedule schedule = scheduleService.getScheduleById(scheduleId);
			alarmService.removeAlarmsBySchedule(schedule);
			imageService.removeImgsBySchedule(schedule);
			scheduleService.removeSchedule(schedule);
			return;
		}
		User user = userService.getUser(userId);
		MoimSchedule moimSchedule = moimScheduleService.getMoimScheduleWithMoimScheduleAndUsers(scheduleId);
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);

		moimScheduleAndUserService.removeMoimScheduleAlarm(moimScheduleAndUser);
		moimScheduleAndUserService.removeMoimScheduleAndUserInPersonalSpace(moimScheduleAndUser);
	}

}
