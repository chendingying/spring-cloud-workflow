package com.spring.cloud.identity.repository;

import com.spring.cloud.common.repository.BaseRepository;
import com.spring.cloud.identity.constant.TableConstant;
import com.spring.cloud.identity.domain.Group;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends BaseRepository<Group, Integer> {
	@Query("select a from Group a, UserGroup b where a.id = b.groupId and a.status=" + TableConstant.ROLE_STATUS_NORMAL + " and b.userId = ?1 order by a.order asc")
	List<Group> findByUserId(int userId);

	List<Group> findByParentId(int parentId);

	List<Group> findByStatusOrderByOrderAsc(byte status);
}