package com.lwj.demo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lwj.demo.system.entity.TenantData;
import com.lwj.demo.system.vo.AddDataSourceReqVo;

import java.util.List;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.dynamic.service
 * @ClassName: TenantIdInfoServiceImpl
 * @CreateDate: 2021/5/27 17:49
 * @Description: 租户数据库连接信息接口
 * @Description: 租户数据库连接信息接口
 */
public interface TenantDataInfoService extends IService<TenantData> {

    /**
     * 获取所有租户连接信息
     *
     * @return java.util.List<com.lwj.demo.dynamic.TenantDataInfo>
     */
    List<TenantData> listAllTenantDataInfo();

    /**
     * 添加租户数据源信息
     *
     * @param reqVo 请求信息
     * @return boolean
     */
    boolean addTenantDataInfo(AddDataSourceReqVo reqVo);
}
