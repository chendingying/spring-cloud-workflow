package com.spring.cloud.flow.rest.instance;

import com.spring.cloud.flow.rest.AbstractPaginateList;
import com.spring.cloud.flow.rest.RestResponseFactory;

import java.util.List;

public class ProcessInstancePaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public ProcessInstancePaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createHistoricProcessInstancResponseList(list);
	}
}
