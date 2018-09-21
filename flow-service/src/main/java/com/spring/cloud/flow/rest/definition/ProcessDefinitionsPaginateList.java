package com.spring.cloud.flow.rest.definition;


import com.spring.cloud.flow.rest.AbstractPaginateList;
import com.spring.cloud.flow.rest.RestResponseFactory;

import java.util.List;


public class ProcessDefinitionsPaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public ProcessDefinitionsPaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createProcessDefinitionResponseList(list);
	}
}
