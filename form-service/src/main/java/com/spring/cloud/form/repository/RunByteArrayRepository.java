package com.spring.cloud.form.repository;

import com.spring.cloud.form.domain.RunByteArray;
import com.spring.cloud.common.repository.BaseRepository;

/**
 * Created by CDZ on 2018/9/15.
 */
public interface RunByteArrayRepository extends BaseRepository<RunByteArray, Integer> {
    RunByteArray findByProcInstId(String procInstId);
}
