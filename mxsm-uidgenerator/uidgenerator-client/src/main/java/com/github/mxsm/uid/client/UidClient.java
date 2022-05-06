package com.github.mxsm.uid.client;

import com.github.mxsm.uid.core.common.SnowflakeUidParsedResult;

/**
 * @author mxsm
 * @date 2022/4/30 15:54
 * @Since 1.0.0
 */
public interface UidClient {

    long getSegmentUid(String bizCode, boolean fromRemote);

    long getSegmentUid(String bizCode);

    long getSnowflakeUid();

    SnowflakeUidParsedResult parseSnowflakeUid(long uid);

    static UidClientBuilder builder(){
        return new UidClientBuilder();
    }

}
