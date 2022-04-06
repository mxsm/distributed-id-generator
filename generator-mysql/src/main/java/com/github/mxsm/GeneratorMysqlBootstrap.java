package com.github.mxsm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mxsm
 * @date 2022/4/5 21:21
 * @Since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.github.mxsm.generator.dao")
public class GeneratorMysqlBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorMysqlBootstrap.class,args);
    }

}
