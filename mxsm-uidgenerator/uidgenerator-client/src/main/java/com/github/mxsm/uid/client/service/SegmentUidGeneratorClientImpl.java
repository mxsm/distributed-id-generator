package com.github.mxsm.uid.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.mxsm.uid.core.SegmentUidGenerator;
import com.github.mxsm.uid.core.common.Result;
import com.github.mxsm.uid.core.exception.UidGenerateException;
import com.github.mxsm.uid.core.segment.Segment;
import com.github.mxsm.uid.core.segment.SegmentConsumerListener;
import com.github.mxsm.uid.core.segment.SegmentPanel;
import com.github.mxsm.uid.core.segment.SegmentUidGeneratorAbstract;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

/**
 * @author mxsm
 * @date 2022/4/30 21:05
 * @Since 1.0.0
 */
public class SegmentUidGeneratorClientImpl extends SegmentUidGeneratorAbstract implements SegmentUidGenerator, SegmentConsumerListener {

    private String uidGeneratorServerUir;

    private int threshold;

    private ExecutorService executorService;

    public SegmentUidGeneratorClientImpl(String uidGeneratorServerUir, int cacheSize, int threshold,
        ExecutorService executorService) {
        super(cacheSize);
        this.uidGeneratorServerUir = uidGeneratorServerUir;
        this.threshold = threshold;
        this.executorService = executorService;
    }

    @Override
    public SegmentPanel createSegmentPanel(String bizCode, int stepSize) {
        List<Segment> segments = getSegments(bizCode, stepSize);
        return new SegmentPanel(bizCode, stepSize, threshold, segments, this);

    }

    /**
     * get step of number steps
     *
     * @param bizCode
     * @param segmentNum
     * @return
     */
    protected List<Segment> getSegments(String bizCode, int segmentNum) {
        try {
            String uri = uidGeneratorServerUir + "/api/v1/segment/step/" + bizCode + "?segmentNum=" + segmentNum;
            Response response = Request.get(uri).execute();
            String content = response.returnContent().asString(StandardCharsets.UTF_8);
            Result<List<Segment>> result = JSON.parseObject(content, new TypeReference<>() {
            });
            return result.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void listener(SegmentPanel segmentPanel, int segmentSize) {
        AsyncHandleTask task = new AsyncHandleTask(segmentPanel, segmentSize);
        if (executorService == null) {
            task.run();
            return;
        }
        executorService.submit(task);
    }

    /**
     * Get a unique ID for segment
     *
     * @param bizCode
     * @return UID
     * @throws UidGenerateException
     */
    @Override
    public long getUID(String bizCode) throws UidGenerateException {
        try {
            String uri = uidGeneratorServerUir + "/api/v1/segment/uid/" + bizCode;
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

    public long getUIDFromLocalCache(String bizCode) throws UidGenerateException {
        return super.getUID(bizCode);
    }

    class AsyncHandleTask implements Runnable {

        private SegmentPanel segmentPanel;

        private int segmentSize;

        public AsyncHandleTask(SegmentPanel segmentPanel, int segmentSize) {

            this.segmentPanel = segmentPanel;
            this.segmentSize = segmentSize;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
         * causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            String bizCode = segmentPanel.getBizCode();
            List<Segment> segments = getSegments(bizCode, segmentSize);
            segmentPanel.addSegment(segments);
            segmentPanel.resetCounter();
        }
    }
}
