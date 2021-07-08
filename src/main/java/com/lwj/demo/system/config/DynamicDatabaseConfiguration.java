package com.lwj.demo.system.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lwj.demo.common.constants.SystemConstant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.dynamic.config
 * @ClassName: MasterDatabaseProperties
 * @CreateDate: 2021/5/28 8:26
 * @Description: 系统数据源
 */
@Configuration
@EnableConfigurationProperties(value = DynamicDatabaseProperties.class)
public class DynamicDatabaseConfiguration {

    private final DynamicDatabaseProperties dynamicDatabaseProperties;

    public DynamicDatabaseConfiguration(DynamicDatabaseProperties dynamicDatabaseProperties) {
        this.dynamicDatabaseProperties = dynamicDatabaseProperties;
    }

    /**
     * 设置数据源，默认一个系统数据源
     *
     * @return javax.sql.DataSource
     */
    @Bean
    @Primary
    public DataSource multipleDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = CollectionUtils.newHashMap(1);
        targetDataSources.put(SystemConstant.ROOT_PARENT_ID, systemDataSource());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(systemDataSource());
        return dynamicDataSource;
    }

    /**
     * 系统数据源，也是默认数据源。连接的是系统数据库，系统数据源必须先加载，才能获取租户的连接信息
     *
     * @return javax.sql.DataSource
     */
    @Bean
    @Primary
    public DataSource systemDataSource() {
        return dynamicDatabaseProperties.getBaseDataSource(null, true);
    }

    /**
     * mybatis的会话工厂，这改成使用自定义动态数据源，覆盖默认的的SqlSessionFactory
     *
     * @return org.apache.ibatis.session.SqlSessionFactory
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource());
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        return sqlSessionFactory.getObject();
    }


    @Bean(name = "multipleTransactionManager")
    @Primary
    public DataSourceTransactionManager multipleTransactionManager(@Qualifier("multipleDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
