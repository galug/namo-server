package com.example.namo2.moim;

import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.moim.dto.GetMoimRes;
import com.example.namo2.moim.dto.PatchMoimName;
import com.example.namo2.moim.dto.PostMoimRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Api(value = "Moims")
public class MoimController {
    private final MoimService moimService;

    @PostMapping("")
    @ApiOperation(value = "모임 생성")
    public BaseResponse<PostMoimRes> createMoim(@RequestPart(required = false) MultipartFile img,
                                                @RequestPart String groupName,
                                                @RequestPart String color,
                                                HttpServletRequest request) {
        Long moimId = moimService.create((Long) request.getAttribute("userId"), groupName, img, color);
        return new BaseResponse(new PostMoimRes(moimId));
    }

    @GetMapping("")
    @ApiOperation(value = "모임 리스트 조회")
    public BaseResponse<List<GetMoimRes>> findMoimList(HttpServletRequest request) {
        List<GetMoimRes> moims = moimService.findMoims((Long) request.getAttribute("userId"));
        return new BaseResponse(moims);
    }

    @PatchMapping("/name")
    @ApiOperation(value = "모임 이름 수정하기(자신만 바뀜)")
    public BaseResponse<Long> updateName(@RequestBody PatchMoimName patchMoimName, HttpServletRequest request) {
        Long moimId = moimService.patchMoimName(patchMoimName, (Long) request.getAttribute("userId"));
        return new BaseResponse(moimId);
    }
}
