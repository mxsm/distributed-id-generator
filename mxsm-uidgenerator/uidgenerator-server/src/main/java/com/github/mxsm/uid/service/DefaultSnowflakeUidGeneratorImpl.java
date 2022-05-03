package com.github.mxsm.uid.service;

import com.github.mxsm.uid.common.DeployEnvType;
import com.github.mxsm.uid.config.SnowflakeUidGeneratorConfig;
import com.github.mxsm.uid.core.snowflake.AbstractSnowflakeUidGenerator;
import com.github.mxsm.uid.core.snowflake.BitsAllocator;
import com.github.mxsm.uid.dao.SnowflakeNodeDao;
import com.github.mxsm.uid.entity.SnowflakeNodeEntity;
import com.github.mxsm.uid.utils.NetUtils;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mxsm
 * @date 2022/5/1 19:43
 * @Since 1.0.0
 */
@Service("defaultSnowflakeUidGeneratorImpl")
public class DefaultSnowflakeUidGeneratorImpl extends AbstractSnowflakeUidGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSnowflakeUidGeneratorImpl.class);

    private static String EPOCH_DEFAULT = "2022-05-01";

    private SnowflakeNodeDao snowflakeNodeDao;

    @Value("${server.port:8080}")
    private int port;

    @Value("${server.address:}")
    private String hostName;

    private DeployEnvType deployEnvType;

    public DefaultSnowflakeUidGeneratorImpl(SnowflakeUidGeneratorConfig config, SnowflakeNodeDao snowflakeNodeDao) {
        super(Optional.ofNullable(config.getEpoch()).orElse(EPOCH_DEFAULT), config.isTimeBitsSecond(),
            new BitsAllocator(config.getTimestampBits(), config.getMachineIdBits(), config.getSequenceBits()));
        this.snowflakeNodeDao = snowflakeNodeDao;
        this.deployEnvType = config.isContainer() ? DeployEnvType.CONTAINER : DeployEnvType.ACTUAL;
        getBitsAllocator().setMachineId(getMachineId());
    }

    @Override
    public long getMachineId() {
        try {
            if (StringUtils.isEmpty(hostName)) {
                hostName = NetUtils.getLocalAddress();
            }
            SnowflakeNodeEntity sf = snowflakeNodeDao.selectSnowflakeNode(NetUtils.address4Long(hostName), port);
            if (sf == null) {
                sf = new SnowflakeNodeEntity();
                sf.setHostName(NetUtils.address4Long(hostName));
                sf.setPort(port);
                sf.setDescription("test");
                sf.setDeployEnvType(deployEnvType);
                snowflakeNodeDao.insertSnowflakeNode(sf);
            }
            long machineId = sf.getId().longValue();
            if ((machineId & getBitsAllocator().getMaxMachineId()) == 0) {
                LOGGER.warn("get machine id from db is {}, greater than machine id max {}", machineId,
                    getBitsAllocator().getMaxMachineId());
                machineId = randomMachineId();
            }
            return machineId;
        } catch (Exception e) {
            LOGGER.error("get machine id from db error", e);
            return randomMachineId();
        }
    }

}
