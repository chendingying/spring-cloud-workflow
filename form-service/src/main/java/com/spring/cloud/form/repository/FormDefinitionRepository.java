package com.spring.cloud.form.repository;

import com.spring.cloud.form.domain.FormDefinition;
import com.spring.cloud.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormDefinitionRepository extends BaseRepository<FormDefinition, Integer> {
	
	@Query("select f from FormDefinition f where f.key = ?1 and (tenantId = ''  or tenantId is null) "
			+ " and version = (select max(version) from FormDefinition where key = ?1 and (tenantId = ''  or tenantId is null))")
	FormDefinition findLatestFormDefinitionByKey(String key);
	
	
	@Query("select f from FormDefinition f where f.key = ?1 and tenantId = ?2 "
			+ " and version = (select max(version) from FormDefinition where key = ?1 and tenantId = ?2)")
	FormDefinition findLatestFormDefinitionByKeyAndTenantId(String key, String tenantId);

	List<FormDefinition> findFeploySourceIdByTableId(Integer id);

	FormDefinition findByKey(String key);

	FormDefinition findByKeyAndTableId(String key,Integer tableId);
}