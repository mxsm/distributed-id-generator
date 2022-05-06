package com.github.mxsm.uid.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mxsm
 * @date 2022/5/5 14:48
 * @Since 1.0.0
 */
public class UidClientTest {

    private UidClient client;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Before
    public void setUp() throws Exception {
        client = UidClient.builder().setUidGeneratorServerUir("172.29.250.21:8080").setMachineIdBits(5).setSequenceBits(17)
            .setSegmentNum(20).setThreshold(30).isSnowflakeUidFromRemote(false).build();
    }

    @Test
    public void getSegmentUidFromCache() {


    }

    @Test
    public void getSegmentUidFromRemote() {
        long uid = client.getSegmentUid("VHHpwr80O9HXVv27GKLsShDH119251376");
        System.out.println(uid);

    }

    @Test
    public void testGetSegmentUidFromRemote() {
    }

    @Test
    public void getSnowflakeUid() {
        long uid = client.getSnowflakeUid();
        System.out.println(uid);
    }

    @Test
    public void builder() {
    }
}