package com.spring.cloud.flow.rest.definition.resource;

import com.spring.cloud.flow.constant.ErrorConstant;
import com.spring.cloud.flow.constant.TableConstant;
import com.spring.cloud.flow.rest.common.IdentityRequest;
import com.spring.cloud.flow.rest.common.IdentityResponse;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程定义授权信息接口
 * @author cdy
 * @create 2018/9/4
 */
@RestController
public class ProcessDefinitionIdentityResource extends BaseProcessDefinitionResource {

	@GetMapping(value = "/process-definitions/{processDefinitionId}/identity-links", name = "流程定义授权查询")
	public List<IdentityResponse>  getIdentitys(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
		return restResponseFactory.createIdentityResponseList(identityLinks);
		
	}

	@PostMapping(value = "/process-definitions/{processDefinitionId}/identity-links", name = "流程定义授权创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional
	public void createIdentity(@PathVariable String processDefinitionId, @RequestBody IdentityRequest authorizeRequest) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		validateIdentityArguments(authorizeRequest.getIdentityId(), authorizeRequest.getType());

		if (TableConstant.IDENTITY_GROUP.equals(authorizeRequest.getType())) {
			repositoryService.addCandidateStarterGroup(processDefinition.getId(), authorizeRequest.getIdentityId());
		} else if (TableConstant.IDENTITY_USER.equals(authorizeRequest.getType())) {
			repositoryService.addCandidateStarterUser(processDefinition.getId(), authorizeRequest.getIdentityId());
		}
		loggerConverter.save("创建了流程定义授权 '" +processDefinition.getName()+ "'");

	}

	@DeleteMapping(value = "/process-definitions/{processDefinitionId}/identity-links/{type}/{id}", name = "流程定义授权删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteIdentity(@PathVariable("processDefinitionId") String processDefinitionId, @PathVariable("id") String id, @PathVariable("type") String type) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		validateIdentityArguments(id, type);

		validateIdentityExists(processDefinitionId, id, type);

		if (TableConstant.IDENTITY_GROUP.equals(type)) {
			repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), id);
		} else if (TableConstant.IDENTITY_USER.equals(type)) {
			repositoryService.deleteCandidateStarterUser(processDefinition.getId(), id);
		}
		loggerConverter.save("删除了流程定义授权 '"+processDefinition.getName()+"'");
	}

	private void validateIdentityArguments(String id, String type) {
		if (id == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.DEFINITION_IDENTITY_ID_NOT_FOUND);
		}
		if (type == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.DEFINITION_IDENTITY_TYPE_NOT_FOUND);
		}

		if (!TableConstant.IDENTITY_GROUP.equals(type) && !TableConstant.IDENTITY_USER.equals(type)) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.DEFINITION_IDENTITY_TYPE_ERROR);
		}
	}

	private void validateIdentityExists(String processDefinitionId, String identityId, String type) {
		List<IdentityLink> allLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
		for (IdentityLink link : allLinks) {
			boolean rightIdentity = false;
			if (TableConstant.IDENTITY_USER.equals(type)) {
				rightIdentity = identityId.equals(link.getUserId());
			} else {
				rightIdentity = identityId.equals(link.getGroupId());
			}

			if (rightIdentity && link.getType().equals(IdentityLinkType.CANDIDATE)) {
				return;
			}
		}
		exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_IDENTITY_NOT_FOUND);
	}
}
