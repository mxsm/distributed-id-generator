package com.github.mxsm.uid.controller;

import com.github.mxsm.uid.core.UidGenerateService;
import com.github.mxsm.uid.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * get uid by bizcode
     * @param bizCode
     * @return
     */
    @RequestMapping("/id/{bizCode}")
    public Result<String> getUid(@PathVariable("bizCode") String bizCode) {
        String uid = uidGenerateService.getUid(bizCode);
        return new Result().buildSuccess(uid);

    }

    /**
     * get bizcode step
     * @param bizCode
     * @param stepNum number of step
     * @return
     */
    @RequestMapping("/step/{bizCode}")
    public String getStep(@PathVariable("bizCode") String bizCode, Integer stepNum) {

        return null;
    }

}
