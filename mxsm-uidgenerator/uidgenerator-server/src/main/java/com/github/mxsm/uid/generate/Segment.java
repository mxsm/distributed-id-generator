package com.github.mxsm.uid.generate;

import com.github.mxsm.uid.exception.SegmentOutOfBoundaryException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mxsm
 * @date 2022/4/21 22:50
 * @Since 1.0.0
 */
public class Segment {

    private long segmentStartNum;

    private int stepLength;

    private AtomicInteger increment = new AtomicInteger(0);

    private volatile boolean isOk = true;

    public Segment(long segmentStartNum, int stepSize) {
        this.segmentStartNum = segmentStartNum;
        this.stepLength = stepSize;
    }

    public long getSegmentUid(){
        int incrementNum = increment.getAndIncrement();
        if(incrementNum >= stepLength){
            isOk = false;
            throw new SegmentOutOfBoundaryException();
        }
        return segmentStartNum + incrementNum;
    }

    public boolean isOk() {
        return isOk;
    }
}
