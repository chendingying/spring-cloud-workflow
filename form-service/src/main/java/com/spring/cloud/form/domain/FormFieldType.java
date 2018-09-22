package com.spring.cloud.form.domain;

import com.spring.cloud.common.domain.BaseEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by CDZ on 2018/9/21.
 */
@Entity
@Table(name = "pw_fo_field_type", catalog = "plumdo_form")
@NamedQuery(name = "FormFieldType.findAll", query = "SELECT f FROM FormFieldType f")
public class FormFieldType extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String key;
    private String type;
    private String status;
    private String remark;

    public FormFieldType(){}

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if(key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
        }
    }

    @Column(name = "key_")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "type_")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "status_")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "remark_")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
