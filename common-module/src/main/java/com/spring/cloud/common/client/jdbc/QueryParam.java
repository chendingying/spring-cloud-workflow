package com.spring.cloud.common.client.jdbc;

import com.spring.cloud.common.utils.ObjectUtils;

import java.util.Collection;
import java.util.List;

/**
 * 查询参数
 * @author cdy
 * @create 2018/9/4
 */
public class QueryParam {
    private String column;
    private Object value;
    private QueryType type;
    private boolean isNeed;

    public QueryParam() {
    }

    public QueryParam(String column, Object value) {
        this.column = column;
        this.value = value;
        this.type = QueryType.EQ;
        this.isNeed = false;
    }

    public QueryParam(String column, QueryType type) {
        this.column = column;
        this.type = type;
        this.isNeed = false;
    }

    public QueryParam(String column, Object value, QueryType type) {
        this.column = column;
        this.value = value;
        this.type = type;
        this.isNeed = false;
    }

    public QueryParam(String column, Object value, QueryType type, boolean isNeed) {
        this.column = column;
        this.value = value;
        this.type = type;
        this.isNeed = isNeed;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public QueryType getType() {
        return type;
    }

    public void setType(QueryType type) {
        this.type = type;
    }

    public boolean isNeed() {
        return isNeed;
    }

    public void setNeed(boolean isNeed) {
        this.isNeed = isNeed;
    }

    @SuppressWarnings("unchecked")
    public boolean isNeedContains() {
        if (!this.isNeed() && type != QueryType.NULL && type != QueryType.NOT_NULL) {
            if (value instanceof Collection) {
                List<Object> list = (List<Object>) value;
                if (list != null && list.size() > 0) {
                    for (Object item : list) {
                        if (ObjectUtils.isNotEmpty(item)) {
                            return true;
                        }
                    }
                }
            } else if (ObjectUtils.isNotEmpty(value)) {
                return true;
            }
            return false;
        }
        return true;
    }
}
