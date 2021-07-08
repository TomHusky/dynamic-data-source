package com.lwj.demo.tenant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwj.demo.tenant.bean.Test;
import com.lwj.demo.tenant.dao.TestDao;
import com.lwj.demo.tenant.service.TestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系統用戶表 服务实现类
 * </p>
 *
 * @author lwj
 * @since 2020-09-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TestServiceImpl extends ServiceImpl<TestDao, Test> implements TestService {

    @Override
    public Test getTestById(Integer id) {
        return getById(id);
    }
}
