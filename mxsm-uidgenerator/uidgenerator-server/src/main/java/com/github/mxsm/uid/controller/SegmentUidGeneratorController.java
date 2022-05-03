package com.github.mxsm.uid.controller;

import com.github.javafaker.Faker;
import com.github.mxsm.uid.core.common.Result;
import com.github.mxsm.uid.core.segment.Segment;
import com.github.mxsm.uid.service.UidGenerateService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxsm
 * @date 2022/4/17 16:07
 * @Since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/segment")
public class SegmentUidGeneratorController {

    @Autowired
    private UidGenerateService uidGenerateService;

    @PostMapping("/rg")
    public Result<Boolean> registerBizCode(@RequestBody Map<String,String> params){
        String bizCode = params.get("bizCode");
        Integer step = Integer.parseInt(params.get("step"));
        return Result.buildSuccess(uidGenerateService.registerBizCode(bizCode,step));

    }

    /**
     * get uid by bizcode
     *
     * @param bizCode
     * @return
     */
    @GetMapping("/uid/{bizCode}")
    public Result<Long> getUid(@PathVariable("bizCode") String bizCode) {
        long uid = uidGenerateService.getUID(bizCode);
        return new Result().buildSuccess(uid);

    }

    /**
     * get bizcode step
     *
     * @param bizCode
     * @param segmentNum number of step
     * @return
     */
    @GetMapping("/step/{bizCode}")
    public Result<List<Segment>> getStep(@PathVariable("bizCode") String bizCode,
        @RequestParam("segmentNum") Integer segmentNum) {
        List<Segment> segments = uidGenerateService.getSegments(bizCode, segmentNum);
        return new Result().buildSuccess(segments);
    }

}
