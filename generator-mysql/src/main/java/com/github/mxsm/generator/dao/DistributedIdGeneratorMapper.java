package com.github.mxsm.generator.dao;

import com.github.mxsm.generator.entity.DistributedIdGeneratorEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author mxsm
 * @date 2022/4/6 22:52
 * @Since 1.0.0
 */
public interface DistributedIdGeneratorMapper {

    void getDistributedId(@Param("dig") DistributedIdGeneratorEntity dig);

}
