package com.github.mxsm.uid.generate;

import com.github.mxsm.uid.dao.AllocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/4/17 16:28
 * @Since 1.0.0
 */
@Service("uidGenerator")
public class UidGenerator {

    @Autowired
    private AllocationDao allocationDao;

    private UidGenerateCache uidGenerateCache = new UidGenerateCache();

    
    public String getUid(String bizCode){
       return null;
    }



}
