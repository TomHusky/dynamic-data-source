package com.lwj.demo.tenant.controller;

import com.lwj.demo.common.vo.AjaxResult;
import com.lwj.demo.tenant.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lwj
 * @Package: com.lwj.demo.tenant.controller
 * @ClassName: TestController
 * @CreateDate: 2021/5/28 14:24
 * @Description: TODO
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("getTestById")
    public AjaxResult getTestById(Integer id) {
        return AjaxResult.success(testService.getTestById(id));
    }

}
