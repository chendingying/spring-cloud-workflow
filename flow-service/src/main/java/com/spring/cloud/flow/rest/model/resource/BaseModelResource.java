package com.spring.cloud.flow.rest.model.resource;


import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.flow.rest.RestResponseFactory;
import com.spring.cloud.flow.rest.log.LoggerConverter;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 模型资源基类
 *
 * @author cdy
 * @create 2018/9/4
 */
public class BaseModelResource extends BaseResource {
    @Autowired
    protected RestResponseFactory restResponseFactory;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected ManagementService managementService;
    @Autowired
    protected LoggerConverter loggerConverter;

    protected Model getModelFromRequest(String modelId) {
        Model model = repositoryService.getModel(modelId);
        if (model == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.MODEL_NOT_FOUND, modelId);
        }
        return model;
    }

    protected void checkModelKeyExists(String modelKey) {
        long countNum = repositoryService.createModelQuery().modelKey(modelKey).count();
        if (countNum > 0) {
            exceptionFactory.throwForbidden(ErrorConstant.MODEL_KEY_ALREADY_EXISTS);
        }
    }
}
