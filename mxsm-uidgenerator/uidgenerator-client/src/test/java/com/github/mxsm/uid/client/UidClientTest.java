package com.github.mxsm.uid.client;

import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;

/**
 * @author mxsm
 * @date 2022/5/2 22:17
 * @Since 1.0.0
 */
public class UidClientTest extends TestCase {

    private UidClient client;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void setUp() throws Exception {
        client = UidClient.builder().setSegmentNum(10).setUidGeneratorServerUir("http://172.29.169.29:8080").build();

    }

    public void testGetUIDFromLocalCache() {
        long uidFromLocalCache = client.getSegmentUidFromCache("1");
        System.out.println(uidFromLocalCache);
    }

    public void testGetUIDFromRemote() {
        long uidFromRemote = client.getSnowflakeUidFromRemote();
        System.out.println(uidFromRemote);
    }

    public void testTestGetUIDFromRemote() {
        long uidFromLocalCache = client.getSnowflakeUidFromRemote("1");
        System.out.println(uidFromLocalCache);
    }

    public void testShutdown() {
    }

    public void testBuilder() throws Exception {

        String uri = "http://172.29.169.29:8080/api/v1/segment/rg";

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            for (int i = 0; i < 5; ++i) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        Faker faker = new Faker();
                        for (int i = 0; i < 20000000; ++i) {
                            String bizCode = faker.internet().password(5, 20, true, false);
                            String step = faker.number().numberBetween(100000, 2000000) + "";
                            HttpPost httpPost = new HttpPost(uri);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("bizCode", bizCode);
                            jsonObject.put("step", step);
                            httpPost.setEntity(new ByteArrayEntity(jsonObject.toJSONString().getBytes(
                                StandardCharsets.UTF_8), ContentType.APPLICATION_JSON));
                            httpPost.setHeader("content-type", "application/json");

                            try {
                                try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        TimeUnit.SECONDS.sleep(100);

/*        try {
            for(int i = 0; i < 10000000; ++i){
                String bizCode = faker.internet().password(5, 20, true, false);
                String step = faker.number().numberBetween(100000, 2000000)+"";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bizCode", bizCode);
                jsonObject.put("step", step);
                Response response = Request.post(uri).addHeader("content-type", "application/json").bodyByteArray(jsonObject.toJSONString().getBytes(
                    StandardCharsets.UTF_8)).execute();
                System.out.println(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

/*    public static void main(String[] args) throws Exception {
        Faker faker = new Faker();
        String sql = "INSERT INTO mxsm_allocation (biz_code, step, create_time, update_time) VALUES ";
        String sqla = "('%s', %d, now(), now())";
        Set<String> sqls = new HashSet<>();
        Set<String> sql1 = new HashSet<>();
        for(int i = 1; i <= 20000000; ++i){
            String bizCode = faker.internet().password(5, 30, true, false)+i;
            int step = faker.number().numberBetween(100000, 2000000);
            if(i % 100 != 0){
                sql1.add(String.format(sqla, bizCode,step));
                continue;
            }
            StringBuilder builder = new StringBuilder(sql);
            builder.append(sql1.stream().collect(Collectors.joining(","))).append(";");
            sqls.add(builder.toString());
            sql1.clear();
        }
        StringBuilder builder = new StringBuilder(sql);
        builder.append(sql1.stream().collect(Collectors.joining(",")));
        sqls.add(builder.toString());
        FileUtils.writeLines(new File("C:\\Users\\mxsm\\Desktop\\coredns\\sql.sql"),sqls);
    }*/
}


