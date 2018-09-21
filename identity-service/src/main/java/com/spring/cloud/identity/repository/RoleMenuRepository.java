package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.domain.RoleMenu;
import org.springframework.transaction.annotation.Transactional;

public interface RoleMenuRepository extends BaseRepository<RoleMenu, Integer> {

	@Transactional
	int deleteByRoleId(int roleId);

	@Transactional
	int deleteByMenuIdAndRoleId(int menuId, int roleId);

}