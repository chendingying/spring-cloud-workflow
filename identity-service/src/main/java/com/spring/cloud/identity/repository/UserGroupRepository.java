package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.domain.UserGroup;
import org.springframework.transaction.annotation.Transactional;

public interface UserGroupRepository extends BaseRepository<UserGroup, Integer> {

	@Transactional
	int deleteByUserId(int userId);

	@Transactional
	int deleteByGroupIdAndUserId(int groupId, int userId);
}