package com.spring.cloud.flow.rest.model.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @author cdy
 * @create 2018/9/4
 */
@RestController
public class ModelImageResource extends BaseModelResource {
    @GetMapping(value = "/models/{modelId}/image", name = "获取模型流程图")
    public ResponseEntity<byte[]> getModelImage(@PathVariable String modelId) throws UnsupportedEncodingException {
        Model model = getModelFromRequest(modelId);
        byte[] imageBytes = repositoryService.getModelEditorSourceExtra(model.getId());
        if (imageBytes == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.MODEL_IMAGE_NOT_FOUND, model.getId());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_PNG);
        try {
            return new ResponseEntity<byte[]>(imageBytes, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_IMAGE_READ_ERROR, e.getMessage());
        }
        return null;
    }
}
