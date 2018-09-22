package com.spring.cloud.identity.resource;

import com.spring.cloud.common.resource.BaseResource;
import com.spring.cloud.identity.constant.ErrorConstant;
import com.spring.cloud.identity.domain.Logger;
import com.spring.cloud.identity.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 日志
 * Created by CDZ on 2018/9/21.
 */
@RestController
public class LoggerResource extends BaseResource {

    @Autowired
    LoggerRepository loggerRepository;

    private Logger getLoggerFromRequest(Integer id) {
        Logger logger = loggerRepository.findOne(id);
        if (logger == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.MENU_NOT_FOUND);
        }
        return logger;
    }

    @GetMapping(value = "/loggers/userId/{userId}")
    public List<Logger> getUserLoggers(@PathVariable String userId){
        return loggerRepository.findByUserId(userId);
    }

    public void saveLogger(Logger logger){
        loggerRepository.save(logger);
    }
}
