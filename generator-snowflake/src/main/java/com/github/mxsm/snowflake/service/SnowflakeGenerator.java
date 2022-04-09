package com.github.mxsm.snowflake.service;

/**
 * @author mxsm
 * @date 2022/4/9 21:17
 * @Since 1.0.0
 */
public class SnowflakeGenerator {

    private static final long FIXED_TIMESTAMP = 1649491204306L;

    private int machineId;

    private int sequenceNumber = 0;

    //最后一次生成ID时间
    private volatile long lastTimestamp = -1L;


    public SnowflakeGenerator(int machineId) {
        this.machineId = machineId;
    }

    public synchronized long nextId() {

        //获取当前时间
        long currentTimestamp = System.currentTimeMillis();

        //同一个毫秒内生成ID
        if(currentTimestamp == lastTimestamp){
            sequenceNumber += 1;
            //处理一秒超过4096个
            if(sequenceNumber > 4096){
                while (currentTimestamp <= lastTimestamp){
                    currentTimestamp = System.currentTimeMillis();
                }
                sequenceNumber = 0;
            }
        }else {
            //重置序列号
            sequenceNumber = 0;
        }
        lastTimestamp = currentTimestamp;

        return ( (currentTimestamp - FIXED_TIMESTAMP) << 22) | (machineId << 12) | sequenceNumber;
    }

}
