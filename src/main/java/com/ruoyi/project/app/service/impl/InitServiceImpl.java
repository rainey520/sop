package com.ruoyi.project.app.service.impl;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.DevId;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.app.domain.Index;
import com.ruoyi.project.app.domain.Init;
import com.ruoyi.project.app.service.IInitService;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.iso.filesource.domain.FileSourceInfo;
import com.ruoyi.project.iso.filesource.mapper.FileSourceInfoMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.system.menu.domain.Menu;
import com.ruoyi.project.system.menu.mapper.MenuMapper;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class InitServiceImpl implements IInitService {

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private FileSourceInfoMapper fileInfoMapper;

    @Autowired
    private DevWorkOrderMapper workOrderMapper;

    @Autowired
    private DevListMapper devListMapper;

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
        // 查询轮播图信息
        List<FileSourceInfo> fileSourceInfos = fileInfoMapper.selectFileListBySaveType(user.getCompanyId(), 0);
        List<String> comPicList = new ArrayList<>();
        for (FileSourceInfo fileSourceInfo : fileSourceInfos) {
            if (fileSourceInfo != null && StringUtils.isNotEmpty(fileSourceInfo.getFilePath())) {
                comPicList.add(fileSourceInfo.getFilePath());
            }
        }
        init.setComPicList(comPicList);
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

    /**
     * 获取工单号
     * @return 结果
     */
    @Override
    public String getWorkCode() {
        String workCode = CodeUtils.getWorkOrderCode();
        DevWorkOrder workOrder = workOrderMapper.selectWorkOrderByCode(workCode);
        if (workOrder != null) {
            workCode = workCode + CodeUtils.getRandom();
        }
        return workCode;
    }

    /**
     * 获取看板编码
     * @return 结果
     */
    @Override
    public String getDevKbCode() {
        // 创建计数器硬件编码
        String kbCode = "KB" + DevId.getPageCode();
        DevList dev = devListMapper.selectDevListByDevId(kbCode);
        if (StringUtils.isNotNull(dev)) {
            throw new BusinessException("生成看板硬件编码失败，请重新生成");
        }
        // 添加计数器硬件信息
        dev = new DevList();
        dev.setDeviceId(kbCode);
        dev.setDeviceStatus(1);
        dev.setDefId(0);
        dev.setDeviceUploadTime(15);
        dev.setDevModelId(DevConstants.DEV_MODEL_KB);
        dev.setCreateDate(new Date());
        devListMapper.insertDevList(dev);
        return kbCode;
    }
}
