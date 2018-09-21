package com.spring.cloud.flow.rest;

import com.spring.cloud.common.resource.PageResponse;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.query.Query;
import org.flowable.engine.common.api.query.QueryProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author cdy
 * @create 2018/9/4
 */
public abstract class AbstractPaginateList {
    @SuppressWarnings("rawtypes")
    public PageResponse paginateList(Pageable pageable, Query query, Map<String, QueryProperty> properties) {
        List list = null;
        if(pageable == null) {
            list = processList(query.list());
        } else {
            setQueryOrder(pageable.getSort(),query,properties);
            list = processList(query.listPage(pageable.getOffset(),pageable.getPageSize()));
        }

        PageResponse response = new PageResponse();
        response.setData(list);
        response.setTotal(query.count());
        return response;
    }

    @SuppressWarnings("rawtypes")
    private void setQueryOrder(Sort sort, Query query, Map<String,QueryProperty> properties) {
        if(sort == null || properties.isEmpty()) {
            return;
        }
        Iterator<Order> orders = sort.iterator();
        while (orders.hasNext()) {
            Order order = orders.next();
            QueryProperty qp = properties.get(order.getProperty());
            if(qp == null) {
                throw new FlowableIllegalArgumentException("Value for param 'sort' is not valid,'" + sort + "' is not a valid property");
            }
            query.orderBy(qp);
            if(order.getDirection() == Direction.ASC) {
                query.asc();
            }else {
                query.desc();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    protected abstract List processList(List list);
}
