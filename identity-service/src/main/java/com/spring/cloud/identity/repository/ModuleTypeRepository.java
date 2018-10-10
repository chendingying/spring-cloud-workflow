package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.domain.ModuleType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by CDZ on 2018/9/18.
 */
public interface ModuleTypeRepository extends BaseRepository<ModuleType, Integer> {
    List<ModuleType> findByParentId(int parentId);
    ModuleType findByModuleTypeAndParentId(String name,Integer parentId);

}
