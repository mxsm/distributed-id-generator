package com.github.mxsm.uid.service;

import com.github.mxsm.uid.core.UidGenerateService;
import com.github.mxsm.uid.generate.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/4/17 16:25
 * @Since 1.0.0
 */
@Service("uidGenerateService")
public class UidGenerateServiceImpl implements UidGenerateService {

    @Autowired
    private UidGenerator uidGenerator;

    /**
     * @param bizCode
     * @return
     */
    @Override
    public String getUid(String bizCode) {
        return uidGenerator.getUid(bizCode);
    }

    /**
     * get step of number steps
     *
     * @param bizCode
     * @param stepNums
     * @return
     */
    @Override
    public long getStep(String bizCode, int stepNums) {
        return 0;
    }
}
