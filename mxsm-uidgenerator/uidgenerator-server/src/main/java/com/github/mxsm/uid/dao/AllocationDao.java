package com.github.mxsm.uid.dao;

import com.github.mxsm.uid.entity.AllocationEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author mxsm
 * @date 2022/4/17 17:00
 * @Since 1.0.0
 */
public interface AllocationDao {

    AllocationEntity getAllocation(@Param("bizCode") String bizCode);

    void insertAllocation(@Param("alloc") AllocationEntity alloc);

    void insertAllocationBatch(@Param("allocs") List<AllocationEntity> allocs);

    void updateAllocation(@Param("stepLength") int stepLength, @Param("bizCode") String bizCode);
}
