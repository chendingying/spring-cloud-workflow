package com.spring.cloud.flow.identity;

import com.spring.cloud.common.client.rest.RestClient;
import com.spring.cloud.common.model.ObjectMap;
import com.spring.cloud.common.utils.ObjectUtils;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.UserQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntity;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityManagerImpl;
import org.flowable.idm.engine.impl.persistence.entity.data.UserDataManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cdy
 * @create 2018/9/4
 */
public class AiaUserEntityManager extends UserEntityManagerImpl {
    private RestClient restClient;

    public AiaUserEntityManager(RestClient restClient, IdmEngineConfiguration idmEngineConfiguration, UserDataManager userDataManager) {
        super(idmEngineConfiguration, userDataManager);
        this.restClient = restClient;
    }

    @Override
    public UserEntity findById(String entityId) {
        return restClient.getForIdentityService("/users/" + entityId, UserEntity.class);
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query) {
        List<User> users = new ArrayList<User>();
        ObjectMap response = restClient.getForIdentityService("/users", queryToParams(query), ObjectMap.class);
        List<ObjectMap> dataMap = response.getAsList("data");
        for (ObjectMap userMap : dataMap) {
            User user = new UserEntityImpl();
            user.setId(userMap.getAsString("id"));
            user.setFirstName(userMap.getAsString("name"));
            user.setLastName(userMap.getAsString("name"));
            user.setEmail(userMap.getAsString("email"));
            users.add(user);
        }
        return users;
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        ObjectMap response = restClient.getForIdentityService("/groups", queryToParams(query), ObjectMap.class);
        return response.getAsLong("total");
    }

    private MultiValueMap<String, String> queryToParams(UserQueryImpl query) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        if(query.getFirstName() != null) {
            queryParams.add("name", query.getFirstName());
        }
        if(query.getFirstNameLike() != null) {
            queryParams.add("name", query.getFirstNameLike());
        }

        if(query.getId() != null) {
            queryParams.add("id", query.getId());
        }
        return queryParams;
    }
}