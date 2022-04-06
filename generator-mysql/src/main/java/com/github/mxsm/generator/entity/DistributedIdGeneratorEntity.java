package com.github.mxsm.generator.entity;

/**
 * @author mxsm
 * @date 2022/4/6 22:51
 * @Since 1.0.0
 */
public class DistributedIdGeneratorEntity {

    private Long id;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
