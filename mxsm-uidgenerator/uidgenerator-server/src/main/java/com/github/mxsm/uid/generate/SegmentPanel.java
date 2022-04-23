package com.github.mxsm.uid.generate;

import com.github.mxsm.uid.exception.SegmentOutOfBoundaryException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mxsm
 * @date 2022/4/21 22:59
 * @Since 1.0.0
 */
public class SegmentPanel {

    private Logger LOGGER = LoggerFactory.getLogger(SegmentPanel.class);

    private BlockingQueue<Segment> segmentQueue;

    private volatile Segment currentSegment;

    private Lock lock = new ReentrantLock();

    private SegmentConsumerListener listener;

    private String bizCode;

    private  int threshold = 50;

    private volatile int counter = 1;

    private int capacity;

    public SegmentPanel(String bizCode,int capacity, List<Segment> segments, SegmentConsumerListener listener) {
        this.bizCode = bizCode;
        this.segmentQueue = new ArrayBlockingQueue<>(capacity);
        this.capacity = capacity;
        this.segmentQueue.addAll(segments);
        this.listener = listener;
        this.currentSegment = this.segmentQueue.poll();

    }

    public long getUid() {
        while (true){
            try {
                return this.currentSegment.getSegmentUid();
            } catch (Exception e) {
                if (e instanceof SegmentOutOfBoundaryException) {
                    try {
                        lock.lock();
                        if (!this.currentSegment.isOk()) {
                            try {
                                this.currentSegment = segmentQueue.take();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            ++counter;
                            if(((capacity-counter) * 100) / capacity <= 50){
                                listener.listener(this, counter);
                                counter = 0;
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }

    public void addSegment(Segment segment){
        this.segmentQueue.offer(segment);
    }
    public void addSegment(List<Segment> segments){
        for(Segment segment : segments){
            this.addSegment(segment);
        }
    }

    public String getBizCode() {
        return bizCode;
    }

}
