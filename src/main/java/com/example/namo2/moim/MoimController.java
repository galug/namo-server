package com.example.namo2.moim;

import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.moim.dto.GetMoimRes;
import com.example.namo2.moim.dto.PostMoimRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/moims")
public class MoimController {
    private final MoimService moimService;

    @PostMapping("")
    public BaseResponse<PostMoimRes> createMoim(@RequestPart(required = false) MultipartFile img,
                                                @RequestPart String groupName,
                                                @RequestPart String color,
                                                HttpServletRequest request) {
        Long moimId = moimService.create((Long) request.getAttribute("userId"), groupName, img, color);
        return new BaseResponse(new PostMoimRes(moimId));
    }

    @GetMapping("")
    public BaseResponse<List<GetMoimRes>> findMoimList(HttpServletRequest request) {
        List<GetMoimRes> moims = moimService.findMoims((Long) request.getAttribute("userId"));
        return new BaseResponse(moims);
    }
}
