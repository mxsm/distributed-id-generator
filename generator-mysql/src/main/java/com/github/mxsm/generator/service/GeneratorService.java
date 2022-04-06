package com.github.mxsm.generator.service;

import com.github.mxsm.generator.dao.DistributedIdGeneratorMapper;
import com.github.mxsm.generator.entity.DistributedIdGeneratorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/4/6 22:49
 * @Since 1.0.0
 */
@Service
public class GeneratorService {

    @Autowired
    private DistributedIdGeneratorMapper mapper;

    public Long getDistributedId(){
        DistributedIdGeneratorEntity dig = new DistributedIdGeneratorEntity();
        dig.setRemark("当前时间："+System.currentTimeMillis());
        mapper.getDistributedId(dig);
        return dig.getId();
    }

}
