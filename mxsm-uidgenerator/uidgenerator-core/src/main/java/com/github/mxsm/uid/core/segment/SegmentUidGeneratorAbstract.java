package com.github.mxsm.uid.core.segment;

import com.github.mxsm.uid.core.SegmentUidGenerator;

/**
 * @author mxsm
 * @date 2022/5/1 16:13
 * @Since 1.0.0
 */
public abstract class SegmentUidGeneratorAbstract implements SegmentUidGenerator {

    private SegmentUidGeneratorCache uidGenerateCache;

    private int cacheSize;

    public SegmentUidGeneratorAbstract(int cacheSize) {
        this.uidGenerateCache = new SegmentUidGeneratorCacheDefaultImpl();
        this.cacheSize = cacheSize;
    }

    @Override
    public long getUID(String bizCode) {
        long uid = uidGenerateCache.getUidFromCacheOrElse(bizCode,
            () -> createSegmentPanel(bizCode, this.cacheSize));
        return uid;
    }

    public abstract SegmentPanel createSegmentPanel(String bizCode, int stepSize);

}
