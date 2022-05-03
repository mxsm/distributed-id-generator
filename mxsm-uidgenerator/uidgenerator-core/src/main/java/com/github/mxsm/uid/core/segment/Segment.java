package com.github.mxsm.uid.core.segment;


import com.github.mxsm.uid.core.exception.SegmentOutOfBoundaryException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mxsm
 * @date 2022/4/21 22:50
 * @Since 1.0.0
 */
public class Segment {

    //this segment start number
    private long segmentStartNum;

    //this segment step size
    private int stepSize;

    private AtomicInteger increment = new AtomicInteger(0);

    //segment status
    private volatile boolean isOk = true;

    public Segment(long segmentStartNum, int stepSize) {
        this.segmentStartNum = segmentStartNum;
        this.stepSize = stepSize;
    }

    public long createSegmentUid(){
        if(!isOk){
            throw new SegmentOutOfBoundaryException();
        }
        int incrementNum = increment.getAndIncrement();
        if(incrementNum >= stepSize){
            isOk = false;
            throw new SegmentOutOfBoundaryException();
        }
        return segmentStartNum + incrementNum;
    }

    public boolean isOk() {
        return isOk;
    }

    public long getSegmentStartNum() {
        return segmentStartNum;
    }

    public void setSegmentStartNum(long segmentStartNum) {
        this.segmentStartNum = segmentStartNum;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }
}
