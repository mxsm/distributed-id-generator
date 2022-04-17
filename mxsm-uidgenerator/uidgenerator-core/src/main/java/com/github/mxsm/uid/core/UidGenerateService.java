package com.github.mxsm.uid.core;

/**
 * @author mxsm
 * @date 2022/4/17 16:16
 * @Since 1.0.0
 */
public interface UidGenerateService {

    /**
     *
     * @param bizCode
     * @return
     */
    String getUid(String bizCode);

    /**
     * get step of number steps
     * @param bizCode
     * @param stepNums
     * @return
     */
    long getStep(String bizCode, int stepNums);

}
