package com.github.mxsm.uid.client;

import com.github.mxsm.uid.client.service.SegmentUidGeneratorClientImpl;
import com.github.mxsm.uid.client.service.SnowflakeUidGeneratorClientImpl;
import com.github.mxsm.uid.core.snowflake.BitsAllocator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mxsm
 * @date 2022/4/30 20:46
 * @Since 1.0.0
 */
public class UidClientImpl implements UidClient {

    private SegmentUidGeneratorClientImpl segmentService;

    private SnowflakeUidGeneratorClientImpl snowflakeService;

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public UidClientImpl(Config config) {

        this.segmentService = new SegmentUidGeneratorClientImpl(config.getUidGeneratorServerUir(),
            config.getSegmentNum(), config.getThreshold(), executorService);
        this.snowflakeService = new SnowflakeUidGeneratorClientImpl(config.getUidGeneratorServerUir(),
            config.getEpoch(), config.isTimeBitsSecond(),config.isSnowflakeLocal(),
            new BitsAllocator(config.getTimestampBits(), config.getMachineIdBits(), config.getSequenceBits()));
    }

    @Override
    public long getSegmentUidFromCache(String bizCode) {
        return segmentService.getUIDFromLocalCache(bizCode);
    }

    @Override
    public long getSnowflakeUidFromRemote(String bizCode) {
        return segmentService.getUID(bizCode);
    }

    @Override
    public long getSnowflakeUidFromRemote() {
        return snowflakeService.getUID();
    }

    @Override
    public long getSnowflakeUidFromLocal() {
        return snowflakeService.getUID();
    }


    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}
