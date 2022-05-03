package com.github.mxsm.uid.controller;

import com.github.mxsm.uid.core.SnowflakeUidGenerator;
import com.github.mxsm.uid.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxsm
 * @date 2022/5/1 17:28
 * @Since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/snowflake")
public class SnowflakeUidGeneratorController {

    @Autowired
    private SnowflakeUidGenerator snowflakeUidGenerator;

    @GetMapping("/uid")
    public Result<Long> getUid() {
        long uid = snowflakeUidGenerator.getUID();
        return uid > 0 ? Result.buildSuccess(uid) : Result.buildError(uid);
    }

    @GetMapping("/parse/{uid}")
    public Result<String> getUid(@PathVariable("uid")Long uid) {
        return Result.buildSuccess(snowflakeUidGenerator.parseUID(uid));
    }

}
