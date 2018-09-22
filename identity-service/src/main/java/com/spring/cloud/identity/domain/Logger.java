package com.spring.cloud.identity.domain;

import com.spring.cloud.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by CDZ on 2018/9/21.
 */

@Entity
@Table(name = "pw_id_logger", catalog="plumdo_identity")
@NamedQuery(name = "Logger.findAll", query = "SELECT l FROM Logger l")
public class Logger extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String desc;

    @Column(name = "user_id_")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "desc_")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
