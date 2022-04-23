package com.github.mxsm.uid.generate;

import com.github.mxsm.uid.dao.AllocationDao;
import com.github.mxsm.uid.entity.AllocationEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mxsm
 * @date 2022/4/17 16:28
 * @Since 1.0.0
 */
@Service("uidGenerator")
public class UidGenerator {

    @Autowired
    private AllocationDao allocationDao;

    private UidGenerateCache uidGenerateCache = new UidGenerateCache();

    @Autowired
    private SegmentConsumerListener listener;

    @Transactional(rollbackFor = Exception.class)
    public String getUid(String bizCode){
        long uid = uidGenerateCache.getUidFromCacheOrElse(bizCode, () -> createSegmentPanel(bizCode, 10));
        return String.valueOf(uid);
    }


    private SegmentPanel createSegmentPanel(String bizCode, int stepSize){

        AllocationEntity allocation = allocationDao.getAllocation(bizCode);
        allocationDao.updateAllocation(stepSize, bizCode);
        Long startUid = allocation.getMaxId();
        Integer stepLength = allocation.getStep();
        List<Segment> segments = new ArrayList<>();
        for(int index = 0; index < stepSize; ++ index){
            Segment segment = new Segment(startUid + stepLength * index, stepLength);
            segments.add(segment);
        }
        return new SegmentPanel(bizCode,stepSize,segments,listener);
    }

}