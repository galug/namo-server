package com.example.namo2.domain.schedule.application.converter;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.schedule.ui.dto.ScheduleRequest;

public class ImageConverter {

	public static Image toImage(String url, Schedule schedule){
		return Image.builder()
			.imgUrl(url)
			.schedule(schedule)
			.build();
	}
}
