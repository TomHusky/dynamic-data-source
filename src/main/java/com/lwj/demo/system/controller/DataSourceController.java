package com.lwj.demo.system.controller;

import com.lwj.demo.common.vo.AjaxResult;
import com.lwj.demo.system.config.MyDataSource;
import com.lwj.demo.system.entity.DataSourceType;
import com.lwj.demo.system.service.TenantDataInfoService;
import com.lwj.demo.system.vo.AddDataSourceReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.tenant.controller
 * @ClassName: TestController
 * @CreateDate: 2021/5/28 14:24
 * @Description: 数据源接口
 */
@RestController
@RequestMapping("/api/dataSource")
@MyDataSource(type = DataSourceType.SYSTEM)
public class DataSourceController {

    @Autowired
    private TenantDataInfoService tenantDataInfoService;

    @GetMapping("addDataSource")
    public AjaxResult addDataSource(@RequestBody @Valid AddDataSourceReqVo reqVo) {
        return AjaxResult.success(tenantDataInfoService.addTenantDataInfo(reqVo));
    }

}
