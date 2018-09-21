package com.spring.cloud.flow.cmd;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Model;

import java.io.Serializable;
import java.util.List;

/**
 * 批量修改模型key
 * @author cdy
 * @create 2018/9/4
 */
public class UpdateModelKeyCmd implements Command<Void>, Serializable {
    private static final long serialVersionUID = 1L;
    private String modelId;
    private String modelKey;

    public UpdateModelKeyCmd(String modelId, String modelKey) {
        this.modelId = modelId;
        this.modelKey = modelKey;
    }

    public Void execute(CommandContext commandContext) {
        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration(commandContext).getRepositoryService();
        Model model = repositoryService.getModel(modelId);
        if (model == null) {
            return null;
        }
        List<Model> models = repositoryService.createModelQuery().modelKey(model.getKey()).list();
        for (Model updateModel : models) {
            if(!updateModel.getId().equals(modelId)) {
                updateModel.setKey(modelKey);
                repositoryService.saveModel(updateModel);
            }
        }

        return null;
    }

}