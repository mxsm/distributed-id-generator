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

    private UidClient client11;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Before
    public void setUp() throws Exception {
        client11 = UidClient.builder().setSegmentNum(10).setUidGeneratorServerUir("http://172.29.250.21:8080").isSnowflakeUidFromRemote(false).build();
    }

    @Test
    public void getSegmentUidFromCache() {

        UidClient client = UidClient.builder()
            .setUidGeneratorServerUir("http://172.29.250.21:8080") //设置服务地址
            .setSegmentNum(10) //设置获取的segment数量
            .setThreshold(20) //设置阈值
            .isSegmentUidFromRemote(false) //设置是否直接从服务器通过Restful接口的方式获取
            .build();
        long uid = client.getSegmentUid("mxsm");
        long uidRemote = client.getSegmentUid("mxsm", true);
    }

    @Test
    public void getSegmentUidFromRemote() {
        long uid = client11.getSegmentUid("VHHpwr80O9HXVv27GKLsShDH119251376");
        System.out.println(uid);

    }

    @Test
    public void testGetSegmentUidFromRemote() {
    }

    @Test
    public void getSnowflakeUid() {
        long uid = client11.getSnowflakeUid();
        System.out.println(uid);
    }

    @Test
    public void builder() {
    }
}