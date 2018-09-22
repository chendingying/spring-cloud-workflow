package com.spring.cloud.identity.form;

import com.spring.cloud.identity.domain.Logger;
import com.spring.cloud.identity.domain.User;

/**
 * Created by CDZ on 2018/9/21.
 */
public class LoggerForm extends Logger{
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
