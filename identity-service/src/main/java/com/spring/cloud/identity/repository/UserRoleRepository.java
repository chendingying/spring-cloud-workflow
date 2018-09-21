package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.domain.UserRole;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {
	@Transactional
	int deleteByUserId(int userId);

	@Transactional
	int deleteByRoleIdAndUserId(int roleId, int userId);

}