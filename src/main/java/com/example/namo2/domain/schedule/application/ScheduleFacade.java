package com.example.namo2.domain.schedule.application;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.convert.PeriodConverter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.category.application.impl.CategoryService;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.moim.MoimService;
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
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final MoimService moimService;
	private final FileUtils fileUtils;

	@Transactional
	public ScheduleResponse.ScheduleIdRes createSchedule(ScheduleRequest.PostScheduleReq req, Long userId)
		throws BaseException {

		User user = userService.getUser(userId);
		Category category = categoryService.getCategory(req.getCategoryId());
		Period period = ScheduleConverter.toPeriod(req);
		Schedule schedule = ScheduleConverter.toSchedule(req, period, user, category);
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		schedule.addAlarms(alarms);
		Schedule saveSchedule = scheduleService.createSchedule(schedule);

		return new ScheduleResponse.ScheduleIdRes(saveSchedule.getId());
	}

	@Transactional
	public ScheduleResponse.ScheduleIdRes createDiary(Long scheduleId, String content, List<MultipartFile> imgs)
		throws BaseException {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.updateDiaryContents(content);
		if (imgs != null) {
			List<String> urls = fileUtils.uploadImages(imgs);
			List<Image> imgList = urls.stream().map(url -> ImageConverter.toImage(url, schedule)).toList();
			imageService.createImgs(imgList);
			schedule.setImgs(imgList);
		}
		return ScheduleResponseConverter.toScheduleIdRes(schedule);
	}
	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleRes> getSchedulesByUser(Long userId, List<LocalDateTime> localDateTimes) throws BaseException {
		User user = userService.getUser(userId);
		return scheduleService.getSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
	}
	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleRes> getMoimSchedulesByUser(Long userId, List<LocalDateTime> localDateTimes) throws BaseException {
		User user = userService.getUser(userId);
		return scheduleService.getMoimSchedulesByUser(user, localDateTimes.get(0), localDateTimes.get(1));
	}
	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleRes> getAllSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllSchedulesByUser(user);
	}
	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleRes> getAllMoimSchedulesByUser(Long userId) {
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
	public List<ScheduleResponse.GetDiaryByUserRes> getAllDiariesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllDiariesByUser(user);
	}
	@Transactional(readOnly = true)
	public ScheduleResponse.GetDiaryByScheduleRes getDiaryBySchedule(Long scheduleId) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		schedule.existDairy(); //다이어리 없으면 exception발생
		List<String> imgUrls = schedule.getImages().stream()
			.map(Image::getImgUrl)
			.collect(Collectors.toList());

		return ScheduleResponseConverter.toGetDiaryByScheduleRes(schedule, imgUrls);
	}
	@Transactional
	public ScheduleResponse.ScheduleIdRes modifySchedule(
		Long scheduleId,
		ScheduleRequest.PostScheduleReq req
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
	public void removeSchedule(Long scheduleId, Integer kind, Long userId) throws BaseException {
		if (kind == 0) {//개인 스케줄
			Schedule schedule = scheduleService.getScheduleById(scheduleId);
			scheduleService.removeSchedule(schedule);
			return;
		}
		User user = userService.getUser(userId);
		MoimSchedule moimSchedule = moimService.getMoimScheduleById(scheduleId);
		MoimScheduleAndUser moimScheduleAndUser = moimService.getMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user);

		// 마지막 사람이면 모임 스케줄 삭제
		if (moimSchedule.isLastScheduleMember()) {
			moimService.deleteSchedule(scheduleId);
			return;
		}
		moimService.removeMoimScheduleAndUser(moimScheduleAndUser);
	}
}
