package com.github.mxsm.uid.core.snowflake;

import com.github.mxsm.uid.core.SnowflakeUidGenerator;
import com.github.mxsm.uid.core.exception.UidGenerateException;
import com.github.mxsm.uid.core.utils.DateUtils;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mxsm
 * @date 2022/5/3 16:09
 * @Since 1.0.0
 */
public abstract class AbstractSnowflakeUidGenerator implements SnowflakeUidGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSnowflakeUidGenerator.class);


    private String epoch;

    private long epochTime;

    private volatile long lastTimestamp = -1L;

    private volatile long seqNum = 0;

    private boolean timeBitsSecond;

    private BitsAllocator bitsAllocator;

    public AbstractSnowflakeUidGenerator(String epoch, boolean timeBitsSecond, BitsAllocator bitsAllocator) {
        this.epoch = epoch;
        this.timeBitsSecond = timeBitsSecond;
        this.bitsAllocator = bitsAllocator;
        long epochMill = DateUtils.parseDate(epoch, "yyyy-MM-dd").getTime();
        this.epochTime = timeBitsSecond ? TimeUnit.MILLISECONDS.toSeconds(epochMill) : epochMill;
    }

    public BitsAllocator getBitsAllocator() {
        return bitsAllocator;
    }

    /**
     * Get a unique ID for snowflake
     *
     * @return UID
     * @throws UidGenerateException
     */
    @Override
    public long getUID() throws UidGenerateException {
        return timeBitsSecond ? nextIdExt() : nextIdStandard();
    }

    /**
     * parse uid to string
     *
     * @param uid
     * @return
     */
    @Override
    public String parseUID(long uid) {
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getMachineIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long machineId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long deltaTime = uid >>> (workerIdBits + sequenceBits);

        Date thatTime = new Date(timeBitsSecond?TimeUnit.SECONDS.toMillis(epochTime + deltaTime):epochTime+deltaTime);
        String thatTimeStr = DateUtils.formatByDateTimePattern(thatTime);

        // format as string
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"machineId\":\"%d\",\"sequence\":\"%d\"}",
            uid, thatTimeStr, machineId, sequence);
    }

    public  abstract long getMachineId();

    private synchronized long nextIdStandard() {
        long currentTimestamp = System.currentTimeMillis();
        //Time the callback
        if (currentTimestamp < lastTimestamp) {
            long offset = currentTimestamp - lastTimestamp;
            if (offset <= 1000L) {
                try {
                    wait(offset);
                    currentTimestamp = System.currentTimeMillis();
                    if (currentTimestamp < lastTimestamp) {
                        return -1;
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("wait interrupted error", e);
                    return -2;
                }
            } else {
                // offset > 1000ms
                LOGGER.error("Time rollback exceeds 1 second");
                return -3;
            }
        }
        if (currentTimestamp == lastTimestamp) {
            seqNum = (seqNum + 1) & bitsAllocator.getMaxSequence();
            if (seqNum == 0) {
                seqNum = 0;
                currentTimestamp = System.currentTimeMillis();
                while (currentTimestamp <= lastTimestamp) {
                    currentTimestamp = System.currentTimeMillis();
                }
            }
        } else {
            //reset seqNum
            seqNum = 0;
        }
        lastTimestamp = currentTimestamp;
        return bitsAllocator.allocate(currentTimestamp - epochTime, seqNum);
    }

    private synchronized long nextIdExt() {

        long currentSecond = getCurrentSecond();

        // Clock moved backwards, refuse to generate uid
        if (currentSecond < lastTimestamp) {
            long refusedSeconds = lastTimestamp - currentSecond;
            LOGGER.error("Time rollback exceeds "+refusedSeconds+" second");
            return -2;
        }

        // At the same second, increase sequence
        if (currentSecond == lastTimestamp) {
            LOGGER.info("currentSecond={}",currentSecond);
            seqNum = (seqNum + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            if (seqNum == 0) {
                currentSecond = getNextSecond(lastTimestamp);
            }
        } else {
            // At the different second, sequence restart from zero
            seqNum = 0L;
        }
        lastTimestamp = currentSecond;
        // Allocate bits for UID
        return bitsAllocator.allocate(currentSecond - epochTime, seqNum);
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochTime > bitsAllocator.getMaxTimestamp()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond + ", epoch: " + epoch);
        }
        return currentSecond;
    }

    protected long randomMachineId() {
        long machineId = (long) (Math.random() * getBitsAllocator().getMaxMachineId());
        LOGGER.info("Random machine id is {}", machineId);
        return machineId;
    }

}
