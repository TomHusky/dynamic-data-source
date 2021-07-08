package com.lwj.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwj.demo.exception.BaseException;
import com.lwj.demo.exception.ExceptionCode;
import com.lwj.demo.system.entity.Tenant;
import com.lwj.demo.system.dao.TenantDao;
import com.lwj.demo.system.service.RoleService;
import com.lwj.demo.system.service.TenantService;
import com.lwj.demo.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * saas租户 服务实现类
 * </p>
 *
 * @author lwj
 * @since 2020-09-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TenantServiceImpl extends ServiceImpl<TenantDao, Tenant> implements TenantService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Override
    public boolean addTenant(Tenant tenant) {
        Tenant tenantByName = getTenantByName(tenant.getTenantName());
        if (tenantByName != null) {
            throw new BaseException("租户名称已存在");
        }
        if (checkMobileExists(tenant.getPhone())) {
            throw new BaseException("电话号码已经被其他租户绑定");
        }
        return save(tenant);
    }

    @Override
    public boolean editTenant(Tenant tenant) {
        tenant.setIsDel(null);
        return updateById(tenant);
    }

    @Override
    public boolean deleteTenant(Integer id) {
        Tenant tenant = getTenantById(id);
        if (tenant == null) {
            throw new BaseException("租户不存在");
        }
        tenant.setIsDel(true);
        return updateById(tenant);
    }

    @Override
    public Tenant getTenantById(Integer id) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Tenant::getId, id);
        queryWrapper.lambda().eq(Tenant::getIsDel, false);
        return getOne(queryWrapper);
    }

    @Override
    public List<Tenant> listAllTenant() {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Tenant::getIsDel, false);
        return this.list(queryWrapper);
    }


    @Override
    public Tenant getTenantByName(String tenantName) {
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Tenant::getTenantName, tenantName);
        queryWrapper.lambda().eq(Tenant::getIsDel, false);
        return getOne(queryWrapper);
    }

    @Override
    public boolean checkMobileExists(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new BaseException(ExceptionCode.OPERATE.getCode(), "手机号不能为空");
        }
        QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Tenant::getPhone, mobile);
        queryWrapper.lambda().eq(Tenant::getIsDel, false);
        return this.getOne(queryWrapper) != null;
    }
}
