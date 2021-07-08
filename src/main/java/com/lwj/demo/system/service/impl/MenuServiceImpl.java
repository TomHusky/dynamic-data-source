package com.lwj.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwj.demo.common.constants.SystemConstant;
import com.lwj.demo.exception.BaseException;
import com.lwj.demo.exception.ExceptionCode;
import com.lwj.demo.system.dao.MenuDao;
import com.lwj.demo.system.entity.Menu;
import com.lwj.demo.system.service.MenuService;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author lwj
 * @since 2020-09-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {


    @Override
    public List<Menu> listByPid(Integer parentId) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Menu::getIsDel, false);
        if (parentId == null) {
            parentId = SystemConstant.ROOT_PARENT_ID;
        }
        wrapper.lambda().eq(Menu::getParentId, parentId);
        wrapper.lambda().orderByAsc(Menu::getListOrder);
        return list(wrapper);
    }

    @Override
    public List<Menu> listByPidInTree(Integer parentId) {
        List<Menu> menus = listByPid(parentId);
        menus.forEach(menu -> menu.setChildren(listByPidInTree(menu.getId())));
        return menus;
    }

    @Override
    public List<Menu> listAll() {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Menu::getIsDel, false);
        wrapper.lambda().orderByAsc(Menu::getListOrder);
        return list(wrapper);
    }

    @Override
    public Menu addMenu(Menu menu) {
        if (menu.getListOrder() == null) {
            menu.setListOrder(1);
        }
        if (menu.getParentId() == null) {
            menu.setParentId(SystemConstant.ROOT_PARENT_ID);
        }
        boolean save = this.save(menu);
        if (save) {
            return menu;
        }
        return null;
    }

    @Override
    public Menu getMenuById(Integer id) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Menu::getIsDel, false);
        wrapper.lambda().eq(Menu::getId, id);
        return getOne(wrapper);
    }

    @Override
    public boolean editMenu(Menu menu) {
        Validate.notNull(menu.getId(), "menu id 不允许为空");
        Menu menuById = getMenuById(menu.getId());
        if (menuById == null) {
            throw new BaseException("菜单不存在");
        }
        if (menu.getParentId() == null) {
            menu.setParentId(SystemConstant.ROOT_PARENT_ID);
        }
        return updateById(menu);
    }


    @Override
    public boolean deleteMenu(Integer id) {
        Validate.notNull(id, "menu id 不允许为空");
        Menu menu = getMenuById(id);
        if (menu == null) {
            throw new BaseException(ExceptionCode.DELETE.getCode(), "菜单不存在");
        }
        menu.setIsDel(true);
        return updateById(menu);
    }

}
