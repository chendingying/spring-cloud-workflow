package com.spring.cloud.form.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.form.domain.FormFieldType;

/**
 * Created by CDZ on 2018/9/21.
 */
public interface FormFieldTypeRepository extends BaseRepository<FormFieldType, Integer> {
    FormFieldType findByName(String name);
}
