package com.spring.cloud.identity.domain;

import com.spring.cloud.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by CDZ on 2018/9/18.
 */
@Entity
@Table(name = "pw_id_module_type", catalog = "plumdo_identity")
@NamedQuery(name = "ModuleType.findAll", query = "SELECT r FROM ModuleType r")
public class ModuleType extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String moduleType;
    private Integer parentId;
    private byte status;
    private String remark;
    public ModuleType(){}

    @Column(name = "module_type_", nullable = false, length = 64)
    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    @Column(name = "parent_id_", nullable = false)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "status_", nullable = false)
    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Column(name = "remark_", length = 500)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
