package com.spring.cloud.flow.rest.model;

import com.spring.cloud.flow.rest.AbstractPaginateList;
import com.spring.cloud.flow.rest.RestResponseFactory;

import java.util.List;


public class ModelsPaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public ModelsPaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createModelResponseList(list);
	}
}
