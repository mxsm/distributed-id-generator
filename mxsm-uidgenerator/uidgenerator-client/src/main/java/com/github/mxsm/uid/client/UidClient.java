package com.github.mxsm.uid.client;

/**
 * @author mxsm
 * @date 2022/4/30 15:54
 * @Since 1.0.0
 */
public interface UidClient {

    long getSegmentUidFromCache(String bizCode);

    long getSnowflakeUidFromRemote(String bizCode);

    long getSnowflakeUidFromRemote();

    long getSnowflakeUidFromLocal();

    void shutdown();

    static UidClientBuilder builder(){
        return new UidClientBuilder();
    }

}
