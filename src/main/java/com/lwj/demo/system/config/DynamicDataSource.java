package com.lwj.demo.system.config;

import com.lwj.demo.system.vo.LoginInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.dynamic.config
 * @ClassName: DynamicDataSource
 * @CreateDate: 2021/5/28 9:10
 * @Description: 动态切换数据源
 */
public class DynamicDataSource extends AbstractDataSource implements InitializingBean {

    @Nullable
    private Map<Object, Object> targetDataSources;

    @Nullable
    private Object defaultTargetDataSource;

    private boolean lenientFallback = true;

    private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

    @Nullable
    private Map<Object, DataSource> resolvedDataSources;

    @Nullable
    private DataSource resolvedDefaultDataSource;

    public void setTargetDataSources(@NonNull Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    /**
     * 直接加入数据源
     *
     * @param key   数据源的key
     * @param value 数据源
     */
    public synchronized void addDataSources(@NonNull Object key, @NonNull DataSource value) {
        assert this.resolvedDataSources != null;
        DataSource dataSource = this.resolveSpecifiedDataSource(value);
        this.resolvedDataSources.put(key, dataSource);
    }

    public void setDefaultTargetDataSource(@NonNull Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    public void setLenientFallback(boolean lenientFallback) {
        this.lenientFallback = lenientFallback;
    }

    public void setDataSourceLookup(@Nullable DataSourceLookup dataSourceLookup) {
        this.dataSourceLookup = dataSourceLookup != null ? dataSourceLookup : new JndiDataSourceLookup();
    }

    @Override
    public void afterPropertiesSet() {
        if (this.targetDataSources == null) {
            throw new IllegalArgumentException("Property 'targetDataSources' is required");
        } else {
            if (targetDataSources.isEmpty()) {
                return;
            }
            this.resolvedDataSources = CollectionUtils.newHashMap(this.targetDataSources.size());
            this.targetDataSources.forEach((key, value) -> {
                Object lookupKey = this.resolveSpecifiedLookupKey(key);
                DataSource dataSource = this.resolveSpecifiedDataSource(value);
                this.resolvedDataSources.put(lookupKey, dataSource);
            });
            if (this.defaultTargetDataSource != null) {
                this.resolvedDefaultDataSource = this.resolveSpecifiedDataSource(this.defaultTargetDataSource);
            }

        }
    }

    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        if (dataSource instanceof DataSource) {
            return (DataSource) dataSource;
        } else if (dataSource instanceof String) {
            return this.dataSourceLookup.getDataSource((String) dataSource);
        } else {
            throw new IllegalArgumentException("Illegal data source value - only [javax.sql.DataSource] and String supported: " + dataSource);
        }
    }

    public Map<Object, DataSource> getResolvedDataSources() {
        Assert.state(this.resolvedDataSources != null, "DataSources not resolved yet - call afterPropertiesSet");
        return Collections.unmodifiableMap(this.resolvedDataSources);
    }

    @Nullable
    public DataSource getResolvedDefaultDataSource() {
        return this.resolvedDefaultDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.determineTargetDataSource().getConnection(username, password);
    }

    @Override
    @NotNull
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return iface.isInstance(this) ? (T) this : this.determineTargetDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || this.determineTargetDataSource().isWrapperFor(iface);
    }

    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = this.determineCurrentLookupKey();
        DataSource dataSource = this.resolvedDataSources.get(lookupKey);
        if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
            dataSource = this.resolvedDefaultDataSource;
        }
//        try {
//            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
//            String url = druidDataSource.getConnection().getMetaData().getURL();
//            System.out.println("连接地址：" + url);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        } else {
            return dataSource;
        }
    }

    protected Object determineCurrentLookupKey() {
        // 返回要使用的数据源的key
        LoginInfo tenant = LoginInfoHolder.getTenant();
        if (tenant == null) {
            tenant = new LoginInfo();
        }
        return tenant.getTenantId();
    }

}
