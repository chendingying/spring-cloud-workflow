package com.spring.cloud.form.repository;


import com.spring.cloud.form.domain.FormField;
import com.spring.cloud.common.repository.BaseRepository;

import java.util.List;

public interface FormFieldRepository extends BaseRepository<FormField, Integer> {
    List<FormField> findByTableId(int tableId);
}