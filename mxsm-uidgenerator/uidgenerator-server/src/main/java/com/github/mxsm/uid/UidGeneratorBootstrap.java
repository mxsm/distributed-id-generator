package com.github.mxsm.uid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mxsm
 * @date 2022/4/17 16:01
 * @Since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.github.mxsm.uid.dao")
public class UidGeneratorBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(UidGeneratorBootstrap.class, args);
    }

}
