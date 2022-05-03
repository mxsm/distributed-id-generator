package com.github.mxsm.uid.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.mxsm.uid.core.SnowflakeUidGenerator;
import com.github.mxsm.uid.core.common.Result;
import com.github.mxsm.uid.core.exception.UidGenerateException;
import com.github.mxsm.uid.core.snowflake.AbstractSnowflakeUidGenerator;
import com.github.mxsm.uid.core.snowflake.BitsAllocator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

/**
 * @author mxsm
 * @date 2022/5/3 16:45
 * @Since 1.0.0
 */
public class SnowflakeUidGeneratorClientImpl extends AbstractSnowflakeUidGenerator implements SnowflakeUidGenerator {

    private String uidGeneratorServerUir;

    private boolean snowflakeLocal;

    public SnowflakeUidGeneratorClientImpl(String uidGeneratorServerUir, String epoch, boolean timeBitsSecond,boolean snowflakeLocal, BitsAllocator bitsAllocator) {
        super(epoch, timeBitsSecond, bitsAllocator);
        this.uidGeneratorServerUir = uidGeneratorServerUir;
        this.snowflakeLocal = snowflakeLocal;
        bitsAllocator.setMachineId(getMachineId());
    }

    @Override
    public long getMachineId() {
        return randomMachineId();
    }

    /**
     * Get a unique ID for snowflake
     *
     * @return UID
     * @throws UidGenerateException
     */
    @Override
    public long getUID() throws UidGenerateException {
        return snowflakeLocal? super.getUID(): getUidFromRemote();
    }

    public long getUidFromRemote(){
        try {
            String uri = uidGeneratorServerUir + "/api/v1/snowflake/uid";
            Response response = Request.get(uri).execute();
            String content = response.returnContent().asString(StandardCharsets.UTF_8);
            Result<Long> result = JSON.parseObject(content, new TypeReference<>() {
            });
            return result.getData();
        } catch (IOException e) {
            //TODO
        }
        return -1;
    }
}
