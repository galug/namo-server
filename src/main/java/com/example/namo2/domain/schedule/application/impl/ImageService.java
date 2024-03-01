package com.example.namo2.domain.schedule.application.impl;

import com.example.namo2.domain.schedule.dao.repository.ImageRepository;
import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageRepository imageRepository;
	public List<Image> createImgs(List<Image> imgs){
		return imageRepository.saveAll(imgs);
	}
	public void removeImgsBySchedule(Schedule schedule){
		imageRepository.deleteDiaryImages(schedule);
	}
}
