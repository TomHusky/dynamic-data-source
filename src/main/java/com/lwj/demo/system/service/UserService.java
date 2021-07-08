package com.lwj.demo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lwj.demo.system.entity.AuthNode;
import com.lwj.demo.system.entity.Menu;
import com.lwj.demo.system.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系統用戶表 服务类
 * </p>
 *
 * @author lwj
 * @since 2020-09-02
 */
public interface UserService extends IService<User> {

    /**
     * 查询用户关联的菜单数据
     *
     * @param userId 用户id
     * @param type   菜单类型
     * @return java.util.List<cn.greenbon.api.business.system.bean.User>
     */
    List<Menu> listUserMenus(Integer userId, Integer type);

    /**
     * 查询用户关联菜单数据，树形结构
     *
     * @param userId 用户id
     * @param type   菜单类型
     * @return java.util.List<cn.greenbon.api.business.system.bean.User>
     */
    List<Menu> listUserMenusForTree(Integer userId, Integer type);

    /**
     * 新增用户
     *
     * @param user 用户对象
     * @return boolean
     */
    boolean addUser(User user);

    /**
     * 修改用户
     *
     * @param user 用户对象
     * @return boolean
     */
    boolean editUser(User user);

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return boolean
     */
    boolean deleteUser(Integer userId);

    /**
     * 批量删除用户
     *
     * @param ids 用户id集合
     * @return boolean
     */
    boolean batchDelete(List<Integer> ids);

    /**
     * 根据用户id获取用户
     *
     * @param userId 用户id
     * @return cn.greenbon.api.business.system.bean.User
     */
    User getUserById(Integer userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return cn.greenbon.api.business.system.bean.User
     */
    User getUserByName(String username);

    /**
     * 检查手机号是否重复
     *
     * @param mobile 手机
     * @param userId 用户id
     * @return boolean true-重复
     */
    boolean checkMobileExists(String mobile, Integer userId);

    /**
     * 后台用户修改密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return boolean
     */
    boolean editUserPassword(Integer userId, String oldPassword, String newPassword);

    /**
     * 验证用户密码是否正常
     *
     * @param userId   用户id
     * @param password 密码
     * @return boolean
     */
    boolean validUserPassword(Integer userId, String password);

    /**
     * 重置密码
     *
     * @param userId 目标用户id
     * @return boolean
     */
    boolean resetPassword(Integer userId);

}
