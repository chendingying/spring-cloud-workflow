package com.spring.cloud.identity.repository;


import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, Integer> {
	@Query("select a from Role a, UserRole b where a.id = b.roleId and a.status=" + TableConstant.ROLE_STATUS_NORMAL + " and b.userId = ?1 ")
	List<Role> findByUserId(int userId);

	@Query("select a from Role a, RoleMenu b where a.id = b.roleId and b.menuId = ?1 ")
	List<Role> findByMenuId(int menuId);

	List<Role> findByStatus(byte status);
}