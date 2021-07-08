package com.lwj.demo.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lwj.demo.system.entity.Tenant;

import java.util.List;

/**
 * <p>
 * saas租户 服务类
 * </p>
 *
 * @author lwj
 * @since 2020-09-12
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 添加租户
     *
     * @param tenant 租户信息
     * @return boolean
     */
    boolean addTenant(Tenant tenant);

    /**
     * 编辑租户信息
     *
     * @param tenant 租户信息
     * @return boolean
     */
    boolean editTenant(Tenant tenant);

    /**
     * 删除租户
     *
     * @param id 租户id
     * @return boolean
     */
    boolean deleteTenant(Integer id);

    /**
     * 根据租户id获取租户
     *
     * @param id 租户id
     * @return cn.greenbon.api.business.system.bean.Tenant
     */
    Tenant getTenantById(Integer id);

    /**
     * 获取所有租户
     *
     * @return java.util.List<cn.greenbon.api.business.system.bean.Tenant>
     */
    List<Tenant> listAllTenant();


    /**
     * 根据租户名称获取租户
     *
     * @param tenantName 租户名称
     * @return cn.greenbon.api.business.system.bean.Tenant
     */
    Tenant getTenantByName(String tenantName);

    /**
     * 校验租户电话号码是否被绑定
     *
     * @param mobile   电话号码
     * @return boolean
     */
    boolean checkMobileExists(String mobile);
}
