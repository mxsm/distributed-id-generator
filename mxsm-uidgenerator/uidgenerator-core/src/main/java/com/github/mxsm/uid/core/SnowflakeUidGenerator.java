package com.github.mxsm.uid.core;

import com.github.mxsm.uid.core.exception.UidGenerateException;

/**
 * @author mxsm
 * @date 2022/5/1 15:52
 * @Since 1.0.0
 */
public interface SnowflakeUidGenerator {

    /**
     * Get a unique ID for snowflake
     *
     * @return UID
     * @throws UidGenerateException
     */
    long getUID() throws UidGenerateException;


    /**
     * parse uid to string
     * @param uid
     * @return
     */
    String parseUID(long uid);
}
