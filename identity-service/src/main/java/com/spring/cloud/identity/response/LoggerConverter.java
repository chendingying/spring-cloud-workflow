package com.spring.cloud.identity.response;

import com.spring.cloud.common.utils.TokenUserIdUtils;
import com.spring.cloud.identity.domain.Logger;
import com.spring.cloud.identity.domain.User;
import com.spring.cloud.identity.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CDZ on 2018/9/21.
 */
@Service
public class LoggerConverter {
    @Autowired
    LoggerRepository loggerRepository;

    public void loginSave(User user, String desc){
        Logger logger = new Logger();
        logger.setUserId(String.valueOf(user.getId()));
        logger.setDesc(desc);
        loggerRepository.save(logger);
    }

    public void save(String desc){
        TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
        Logger logger = new Logger();
        logger.setUserId(String.valueOf(tokenUserIdUtils.tokenUserId()));
        logger.setDesc(desc);
        loggerRepository.save(logger);
    }
}
