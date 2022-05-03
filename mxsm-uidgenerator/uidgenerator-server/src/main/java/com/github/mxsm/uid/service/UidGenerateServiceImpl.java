package com.github.mxsm.uid.service;

import com.github.mxsm.uid.core.segment.Segment;
import com.github.mxsm.uid.generate.UidGeneratorServer;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/4/17 16:25
 * @Since 1.0.0
 */
@Service("uidGenerateService")
public class UidGenerateServiceImpl implements UidGenerateService {

    private UidGeneratorServer uidGenerator;

    private AllocationService allocationService;

    public UidGenerateServiceImpl(UidGeneratorServer uidGenerator, AllocationService allocationService) {
        this.uidGenerator = uidGenerator;
        this.allocationService = allocationService;
    }

    /**
     * @param bizCode
     * @return
     */
    @Override
    public long getUID(String bizCode) {
        return uidGenerator.getUID(bizCode);
    }

    /**
     * get step of number steps
     *
     * @param bizCode
     * @param segmentNum
     * @return
     */
    @Override
    public List<Segment> getSegments(String bizCode, int segmentNum) {
        return allocationService.getSegments(bizCode, segmentNum);
    }

    @Override
    public boolean registerBizCode(String bizCode, int step) {
        return allocationService.registerBizCode(bizCode,step);
    }

}
