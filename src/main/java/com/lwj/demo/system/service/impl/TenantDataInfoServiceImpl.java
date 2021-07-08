package com.lwj.demo.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwj.demo.exception.BaseException;
import com.lwj.demo.system.config.DynamicDataSource;
import com.lwj.demo.system.config.DynamicDatabaseProperties;
import com.lwj.demo.system.dao.TenantDataInfoDao;
import com.lwj.demo.system.entity.Tenant;
import com.lwj.demo.system.entity.TenantData;
import com.lwj.demo.system.service.TenantDataInfoService;
import com.lwj.demo.system.service.TenantService;
import com.lwj.demo.system.vo.AddDataSourceReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.dynamic.service
 * @ClassName: TenantIdInfoServiceImpl
 * @CreateDate: 2021/5/27 17:49
 * @Description: 租户数据库连接信息实现类
 */
@Service
public class TenantDataInfoServiceImpl extends ServiceImpl<TenantDataInfoDao, TenantData> implements TenantDataInfoService {

    @Autowired
    private TenantService tenantService;

    @Qualifier("multipleDataSource")
    @Autowired
    private DataSource dataSource;

    private final DynamicDatabaseProperties dynamicDatabaseProperties;

    public TenantDataInfoServiceImpl(DynamicDatabaseProperties dynamicDatabaseProperties) {
        this.dynamicDatabaseProperties = dynamicDatabaseProperties;
    }

    @Override
    public List<TenantData> listAllTenantDataInfo() {
        return this.lambdaQuery()
                .eq(TenantData::getIsDel, false)
                .list();
    }

    @Override
    public boolean addTenantDataInfo(AddDataSourceReqVo reqVo) {
        Tenant tenant = tenantService.getTenantById(reqVo.getTenantId());
        if (tenant == null) {
            throw new BaseException("租户不存在");
        }
        TenantData tenantData = new TenantData().setTenantId(reqVo.getTenantId())
                .setUrl(reqVo.getUrl())
                .setUsername(reqVo.getUsername())
                .setPassword(reqVo.getPassword());
        boolean save = this.save(tenantData);
        if (save) {
            DynamicDataSource dynamicDataSource = (DynamicDataSource) dataSource;
            dynamicDataSource.addDataSources(tenant.getId(), dynamicDatabaseProperties.getBaseDataSource(tenantData, false));
        }
        return save;
    }
}
