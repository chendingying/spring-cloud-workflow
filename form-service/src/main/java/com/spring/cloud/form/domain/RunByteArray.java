package com.spring.cloud.form.domain;

import com.spring.cloud.common.domain.BaseEntity;

import javax.persistence.*;

/**
 * Created by CDZ on 2018/9/15.
 */
@Entity
@Table(name = "pw_ru_bytearray", catalog = "liansen_form")
@NamedQuery(name = "RunByteArray.findAll", query = "SELECT b FROM RunByteArray b")
public class RunByteArray extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private byte[] contentByte;
    private String tableKey;
    private String procInstId;

    public RunByteArray() {
    }

    @Lob
    @Column(name = "content_byte_")
    public byte[] getContentByte() {
        return this.contentByte;
    }

    public void setContentByte(byte[] contentByte) {
        this.contentByte = contentByte;
    }

    @Column(name = "table_key")
    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    @Column(name = "proc_inst_id")
    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }
}
