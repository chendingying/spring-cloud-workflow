package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.domain.Logger;

import java.util.List;

/**
 * Created by CDZ on 2018/9/21.
 */
public interface LoggerRepository extends BaseRepository<Logger, Integer> {
    public List<Logger> findByUserId(String userId);
}
