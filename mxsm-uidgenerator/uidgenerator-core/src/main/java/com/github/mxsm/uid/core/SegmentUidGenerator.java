package com.github.mxsm.uid.core;

import com.github.mxsm.uid.core.exception.UidGenerateException;

/**
 * @author mxsm
 * @date 2022/5/1 15:52
 * @Since 1.0.0
 */
public interface SegmentUidGenerator {


    /**
     * Get a unique ID for segment
     *
     * @return UID
     * @throws UidGenerateException
     */
    long getUID(String bizCode) throws UidGenerateException;

}
