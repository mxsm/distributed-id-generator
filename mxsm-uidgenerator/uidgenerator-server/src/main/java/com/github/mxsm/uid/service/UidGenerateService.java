package com.github.mxsm.uid.service;

import com.github.mxsm.uid.core.segment.Segment;
import java.util.List;

/**
 * @author mxsm
 * @date 2022/4/17 16:16
 * @Since 1.0.0
 */
public interface UidGenerateService {

    /**
     * segment uid
     * @param bizCode
     * @return
     */
    long getUID(String bizCode);

    /**
     * get step of number steps
     * @param bizCode
     * @param segmentNum
     * @return
     */
    List<Segment> getSegments(String bizCode, int segmentNum);


    boolean registerBizCode(String bizCode, int step);

}
