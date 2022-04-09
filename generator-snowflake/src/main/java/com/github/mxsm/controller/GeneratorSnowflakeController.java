package com.github.mxsm.controller;

import com.github.mxsm.snowflake.service.GeneratorSnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxsm
 * @date 2022/4/9 21:16
 * @Since 1.0.0
 */
@RestController
@RequestMapping("/generator/snowflake")
public class GeneratorSnowflakeController {

    @Autowired
    private GeneratorSnowflakeService snowflakeService;

    @GetMapping("/id")
    public Long getDistributedId() {
        return snowflakeService.getDistributedId();
    }

}
