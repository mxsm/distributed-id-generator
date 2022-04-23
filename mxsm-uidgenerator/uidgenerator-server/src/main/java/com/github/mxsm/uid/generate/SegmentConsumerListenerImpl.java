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
 * @date 2022/4/23 21:45
 * @Since 1.0.0
 */
@Service("segmentConsumerListenerImpl")
public class SegmentConsumerListenerImpl implements SegmentConsumerListener{

    @Autowired
    private AllocationDao allocationDao;

    @Override
    @Transactional
    public void listener(SegmentPanel segmentPanel, int segmentSize) {
        String bizCode = segmentPanel.getBizCode();
        AllocationEntity allocation = allocationDao.getAllocation(bizCode);
        allocationDao.updateAllocation(segmentSize, bizCode);
        Long startUid = allocation.getMaxId();
        Integer stepLength = allocation.getStep();
        List<Segment> segments = new ArrayList<>();
        for(int index = 0; index < segmentSize; ++ index){
            Segment segment = new Segment(startUid + stepLength * index, stepLength);
            segments.add(segment);
        }
        segmentPanel.addSegment(segments);
    }
}
