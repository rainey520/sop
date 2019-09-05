package com.ruoyi.project.app.service.impl;

import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.app.domain.Index;
import com.ruoyi.project.app.domain.Init;
import com.ruoyi.project.app.service.IInitService;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.system.menu.domain.Menu;
import com.ruoyi.project.system.menu.mapper.MenuMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class InitServiceImpl implements IInitService {

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IUserService userService;

    /**
     * 获取当天工单、菜单权限、公司信息
     * @return
     */
    @Override
    public Init initIndex(Index index) {
        User user = JwtUtil.getUser();
        Init init = new Init();
        //查询当天工单
//        init.setWorkList(devWorkOrderMapper.selectWorkOrderAllToday(user.getCompanyId()));
        init.setWorkList(Collections.emptyList());
        //查询公司
        init.setCompany(devCompanyMapper.selectDevCompanyById(user.getCompanyId()));
        //查询菜单信息
        init.setMenuList(menuMapper.selectMenuListByParentIdAndUserId(user.getUserId().intValue(),index.getParentId()));
        init.setUserPhoto(userService.selectUserById(user.getUserId()).getAvatar());
        return init;
    }

    /**
     * 获取菜单
     * @param index
     * @return
     */
    @Override
    public List<Menu> initMenu(Index index) {
        User user = JwtUtil.getUser();
        return menuMapper.selectMenuListByParentIdAndUserId(user.getUserId().intValue(),index.getParentId());
    }
}
