package com.spring.cloud.identity.domain;


import com.spring.cloud.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_id_user_role database table.
 * 
 */
@Entity
@Table(name = "pw_id_user_role", catalog="liansen_identity")
@NamedQuery(name = "UserRole.findAll", query = "SELECT s FROM UserRole s")
public class UserRole extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int roleId;
	private int userId;

	public UserRole() {
	}

	@Column(name = "role_id_", nullable = false)
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Column(name = "user_id_", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}