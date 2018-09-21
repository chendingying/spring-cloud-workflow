package com.spring.cloud.flow.config;

import com.spring.cloud.common.client.rest.RestClient;
import com.spring.cloud.flow.identity.AiaGroupEntityManager;
import com.spring.cloud.flow.identity.AiaUserEntityManager;
import org.flowable.idm.spring.SpringIdmEngineConfiguration;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.idm.IdmEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 人员配置类
 * @author cdy
 * @create 2018/9/4
 */
@Configuration
public class FlowableIdmConfig extends IdmEngineAutoConfiguration {
    @Autowired
    private RestClient restClient;

    public FlowableIdmConfig(FlowableProperties flowableProperties, FlowableIdmProperties idmProperties) {
        super(flowableProperties, idmProperties);
    }

    @Autowired
    public SpringIdmEngineConfiguration idmEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager) {
        SpringIdmEngineConfiguration configuration = super.idmEngineConfiguration(dataSource, platformTransactionManager);
        configuration.setGroupEntityManager(new AiaGroupEntityManager(restClient,configuration,configuration.getGroupDataManager()));
        configuration.setUserEntityManager(new AiaUserEntityManager(restClient, configuration, configuration.getUserDataManager()));
        return configuration;
    }

}
