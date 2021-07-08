package com.lwj.demo.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lwj.demo.tenant.bean.Test;

/**
 * <p>
 * 系統用戶表 服务类
 * </p>
 *
 * @author lwj
 * @since 2020-09-02
 */
public interface TestService extends IService<Test> {

    Test getTestById(Integer id);
}
