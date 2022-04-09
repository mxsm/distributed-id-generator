package com.github.mxsm.snowflake.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/4/9 21:17
 * @Since 1.0.0
 */
@Service
public class GeneratorSnowflakeService {


    @Value("${machineId:0}")
    private int machineId;

    private SnowflakeGenerator snowflakeGenerator;

    public GeneratorSnowflakeService() {
        snowflakeGenerator = new SnowflakeGenerator(machineId);
    }


    public Long getDistributedId() {
        return  snowflakeGenerator.nextId();
    }

}
