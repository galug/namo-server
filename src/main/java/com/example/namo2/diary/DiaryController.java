package com.example.namo2.diary;

import com.example.namo2.config.BaseException;
import com.example.namo2.config.BaseResponse;
import com.example.namo2.schedule.dto.PostScheduleReq;
import com.example.namo2.schedule.dto.ScheduleDto;
import com.example.namo2.schedule.dto.ScheduleIdRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/diary")
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

//    @ResponseBody
//    @PostMapping("")
//    public BaseResponse<DiaryIdRes> createDiary(@RequestPart(required = false) List<MultipartFile> imgs,
//                                                @RequestPart String scheduleIdx,
//                                                @RequestPart(required = false) String content) {
//        try {
//            diaryService.createDiary(imgs, scheduleIdx, content);
//            return new BaseResponse<>(scheduleIdRes);
//        } catch (BaseException baseException) {
//            System.out.println("baseException.getStatus() = " + baseException.getStatus());
//            return new BaseResponse(baseException.getStatus());
//        }
//    }
}
