package com.spring.cloud.flow.config;

import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.FlowableMailProperties;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 流程引擎配置类
 * @author cdy
 * @create 2018/9/4
 */
@Configuration
public class FlowableEngineConfig extends ProcessEngineAutoConfiguration {

    public FlowableEngineConfig(FlowableProperties flowableProperties, FlowableProcessProperties processProperties, FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties){
        super(flowableProperties,processProperties,idmProperties,mailProperties);
    }
    @Override
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager, ObjectProvider<AsyncExecutor> asyncExecutorProvider)
            throws IOException {
        SpringProcessEngineConfiguration conf = super.springProcessEngineConfiguration(dataSource, platformTransactionManager, asyncExecutorProvider);
        String databaseSchema = conf.getDatabaseSchema();
        conf.setDatabaseCatalog(databaseSchema);
        conf.setDatabaseTablePrefix(databaseSchema + ".");
        conf.setTablePrefixIsSchema(true);
        conf.setActivityFontName("黑体");
        conf.setLabelFontName("黑体");
        conf.setAnnotationFontName("黑体");
        return conf;
    }

}
