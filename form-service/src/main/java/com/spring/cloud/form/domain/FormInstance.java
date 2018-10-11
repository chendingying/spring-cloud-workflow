package com.spring.cloud.form.domain;


import com.spring.cloud.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_fo_instance database table.
 * 
 */
@Entity
@Table(name = "pw_fo_instance", catalog = "liansen_form")
@NamedQuery(name = "FormInstance.findAll", query = "SELECT f FROM FormInstance f")
public class FormInstance extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int formDefinitionId;
	private String relationTable;
	private byte suspensionState;
	private int tableRelationId;

	public FormInstance() {
	}

	@Column(name = "form_definition_id_")
	public int getFormDefinitionId() {
		return this.formDefinitionId;
	}

	public void setFormDefinitionId(int formDefinitionId) {
		this.formDefinitionId = formDefinitionId;
	}

	@Column(name = "relation_table_")
	public String getRelationTable() {
		return this.relationTable;
	}

	public void setRelationTable(String relationTable) {
		this.relationTable = relationTable;
	}

	@Column(name = "suspension_state_")
	public byte getSuspensionState() {
		return this.suspensionState;
	}

	public void setSuspensionState(byte suspensionState) {
		this.suspensionState = suspensionState;
	}

	@Column(name = "table_relation_id_")
	public int getTableRelationId() {
		return this.tableRelationId;
	}

	public void setTableRelationId(int tableRelationId) {
		this.tableRelationId = tableRelationId;
	}

}