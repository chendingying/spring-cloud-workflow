package com.spring.cloud.common.resource;

import java.util.List;

/**
 * 分页查询结果类
 * @author cdy
 * @create 2018/9/4
 */
@SuppressWarnings("rawtypes")
public class PageResponse {
    protected List data;
    protected long total;

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
