package com.spring.cloud.form.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.form.domain.FormLayout;

import java.util.List;

public interface FormLayoutRepository extends BaseRepository<FormLayout, Integer> {
     List<FormLayout> findByTableId(Integer id);
}