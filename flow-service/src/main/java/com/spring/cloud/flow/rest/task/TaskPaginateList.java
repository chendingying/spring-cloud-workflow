package com.spring.cloud.flow.rest.task;

import com.spring.cloud.flow.rest.AbstractPaginateList;
import com.spring.cloud.flow.rest.RestResponseFactory;

import java.util.List;


public class TaskPaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public TaskPaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createHistoricTaskResponseList(list);
	}
}
