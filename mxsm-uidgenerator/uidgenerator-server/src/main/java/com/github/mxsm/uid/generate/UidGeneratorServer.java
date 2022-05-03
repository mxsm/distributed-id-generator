package com.github.mxsm.uid.generate;

import com.github.mxsm.uid.config.SegmentUidGeneratorConfig;
import com.github.mxsm.uid.core.segment.Segment;
import com.github.mxsm.uid.core.segment.SegmentConsumerListener;
import com.github.mxsm.uid.core.segment.SegmentPanel;
import com.github.mxsm.uid.core.segment.SegmentUidGeneratorAbstract;
import com.github.mxsm.uid.service.AllocationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/4/17 16:28
 * @Since 1.0.0
 */
@Service("uidGenerator")
public class UidGeneratorServer extends SegmentUidGeneratorAbstract {

    @Autowired
    private AllocationService allocationService;

    private SegmentUidGeneratorConfig config;

    @Autowired
    private SegmentConsumerListener listener;

    public UidGeneratorServer(SegmentUidGeneratorConfig config) {
        super(config.getCacheSize());
        this.config = config;
    }

    @Override
    public SegmentPanel createSegmentPanel(String bizCode, int stepSize) {
        List<Segment> segments = allocationService.getSegments(bizCode, stepSize);
        return new SegmentPanel(bizCode, stepSize, config.getThreshold(), segments, listener);
    }
}
