package com.github.mxsm.controller;

import com.github.mxsm.generator.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxsm
 * @date 2022/4/6 22:48
 * @Since 1.0.0
 */
@RestController
@RequestMapping("/generator/mysql")
public class Generator4MysqlController {

    @Autowired
    private GeneratorService generatorService;

    @GetMapping("/id")
    public  Long getDistributedId(){
        return generatorService.getDistributedId();
    }
}
