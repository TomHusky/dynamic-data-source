package com.lwj.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwj.demo.common.constants.SystemConstant;
import com.lwj.demo.common.enums.UserStatus;
import com.lwj.demo.exception.BaseException;
import com.lwj.demo.exception.ExceptionCode;
import com.lwj.demo.system.dao.UserDao;
import com.lwj.demo.system.entity.AuthNode;
import com.lwj.demo.system.entity.Menu;
import com.lwj.demo.system.entity.Role;
import com.lwj.demo.system.entity.User;
import com.lwj.demo.system.service.RoleService;
import com.lwj.demo.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public List<Menu> listUserMenus(Integer userId, Integer type) {
        User user = getUserById(userId);
        if (user == null) {
            throw new BaseException("获取用户菜单错误");
        }
        Role role = roleService.getRoleById(user.getRoleId());
        if (role == null) {
            return Collections.emptyList();
        }
        return roleService.listMenusByRole(role.getId(), type);
    }

    @Override
    public List<Menu> listUserMenusForTree(Integer userId, Integer type) {
        List<Menu> menus = listUserMenus(userId, type);
        if (!menus.isEmpty()) {
            menus = menus.stream().filter(item -> item.getType().equals(type)).
                    collect(Collectors.toList());
            List<Menu> menuTree = new ArrayList<>();
            for (Menu menu : menus) {
                if (menu.getParentId() == SystemConstant.ROOT_PARENT_ID) {
                    menuTree.add(menu);
                }
            }
            parseTreeMenus(menus, menuTree);
            return menuTree;
        }
        return Collections.emptyList();
    }


    /**
     * 将普通的列表对象解析成tree格式
     */
    private void parseTreeAuthNodes(List<AuthNode> authNodes, List<AuthNode> authNodeTree) {
        for (AuthNode parentNode : authNodeTree) {
            List<AuthNode> childrenNodes = new ArrayList<>();
            for (AuthNode authNode : authNodes) {
                if (Objects.equals(authNode.getParentId(), parentNode.getId())) {
                    childrenNodes.add(authNode);
                }
            }
            if (!childrenNodes.isEmpty()) {
                parentNode.setChecked(true);
                parentNode.setChildren(childrenNodes);
                parseTreeAuthNodes(authNodes, parentNode.getChildren());
            }
        }
    }

    /**
     * 将普通的列表对象解析成tree格式
     *
     * @param menus    所有的菜单
     * @param menuTree 填充之后的树形菜单
     */
    private void parseTreeMenus(List<Menu> menus, List<Menu> menuTree) {
        for (Menu parentMenu : menuTree) {
            List<Menu> childrenMenus = new ArrayList<>();
            for (Menu menu : menus) {
                if (Objects.equals(menu.getParentId(), parentMenu.getId())) {
                    childrenMenus.add(menu);
                }
            }
            if (!childrenMenus.isEmpty()) {
                parentMenu.setChecked(true);
                parentMenu.setChildren(childrenMenus);
                parseTreeMenus(menus, parentMenu.getChildren());
            }
        }
    }

    @Override
    public boolean addUser(User user) {
        User userByName = getUserByName(user.getUsername());
        if (userByName != null) {
            throw new BaseException(ExceptionCode.INSERT.getCode(), "用户名已存在");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword("123456");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return save(user);
    }

    @Override
    public boolean editUser(User user) {
        if (user == null) {
            return false;
        }
        User userById = getUserById(user.getId());
        if (userById == null) {
            throw new BaseException("用户不存在");
        }
        boolean exists = checkMobileExists(user.getPhone(), user.getId());
        if (exists) {
            throw new BaseException(ExceptionCode.EDIT.getCode(), "手机号码已存在");
        }
        if (user.getRoleId() != null && roleService.getRoleById(user.getRoleId()) == null) {
            throw new BaseException(ExceptionCode.EDIT.getCode(), "修改的角色不存在");
        }
        user.setPassword(null);
        user.setUsername(null);
        return this.updateById(user);
    }


    @Override
    public boolean deleteUser(Integer userId) {
        if (userId != null) {
            User user = this.getUserById(userId);
            if (user == null) {
                throw new BaseException(ExceptionCode.DELETE.getCode(), "用户不存在");
            }
            user.setStatus(UserStatus.DELETED.getValue());
            boolean delete = updateById(user);
            return delete;
        }
        return false;
    }


    @Override
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BaseException(ExceptionCode.DELETE.getCode(), "id不能为空");
        }
        ids.forEach(item -> {
            boolean delete = deleteUser(item);
            if (!delete) {
                throw new BaseException(ExceptionCode.DELETE.getCode(), "删除失败");
            }
        });
        return true;
    }

    @Override
    public User getUserById(Integer userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(User::getStatus, UserStatus.DELETED.getValue());
        queryWrapper.lambda().eq(User::getId, userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public User getUserByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(User::getStatus, UserStatus.DELETED.getValue());
        queryWrapper.lambda().eq(User::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean checkMobileExists(String mobile, Integer userId) {
        if (StringUtils.isEmpty(mobile)) {
            throw new BaseException(ExceptionCode.OPERATE.getCode(), "手机号不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getPhone, mobile);
        if (userId != null) {
            queryWrapper.lambda().ne(User::getId, userId);
        }
        queryWrapper.lambda().ne(User::getStatus, UserStatus.DELETED.getValue());
        return this.getOne(queryWrapper) != null;
    }

    @Override
    public boolean editUserPassword(Integer userId, String oldPassword, String newPassword) {
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new BaseException(ExceptionCode.EDIT.getCode(), "密码不能为空");
        }
        User user = this.getUserById(userId);
        if (user == null) {
            throw new BaseException(ExceptionCode.EDIT.getCode(), "用户不存在");
        }
        boolean validPassword = validUserPassword(user.getId(), oldPassword);
        if (!validPassword) {
            throw new BaseException(ExceptionCode.EDIT.getCode(), "旧密码校验失败");
        }
        if (newPassword.length() < 6) {
            throw new BaseException(ExceptionCode.EDIT.getCode(), "密码长度要大于6位");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return this.updateById(user);
    }

    @Override
    public boolean validUserPassword(Integer userId, String password) {
        User user = this.getUserById(userId);
        if (user == null) {
            throw new BaseException(ExceptionCode.DELETE.getCode(), "用户不存在");
        }
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean resetPassword(Integer userId) {
        User user = this.getUserById(userId);
        if (user == null) {
            throw new BaseException(ExceptionCode.DELETE.getCode(), "用户不存在");
        }
        user.setPassword(bCryptPasswordEncoder.encode("123456"));
        return this.updateById(user);
    }

}
