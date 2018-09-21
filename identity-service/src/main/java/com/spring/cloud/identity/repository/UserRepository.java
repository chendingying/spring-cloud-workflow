package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Integer> {
	User findByAccount(String account);

	@Query("select a from User a, UserRole b where a.id = b.userId and b.roleId = ?1 ")
	List<User> findByRoleId(int roleId);

	@Query("select a from User a, UserGroup b where a.id = b.userId and b.groupId = ?1 ")
	List<User> findByGroupId(int groupId);
}