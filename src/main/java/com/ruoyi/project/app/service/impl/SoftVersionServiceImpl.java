package com.ruoyi.project.app.service.impl;

import com.ruoyi.project.app.domain.SoftVersion;
import com.ruoyi.project.system.config.mapper.SoftVersionMapper;
import com.ruoyi.project.app.service.ISoftVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手机版本服务层
 * @Author: Rainey
 * @Date: 2019/10/9 15:08
 * @Version: 1.0
 **/
@Service
public class SoftVersionServiceImpl implements ISoftVersionService {

    @Autowired
    private SoftVersionMapper softVersionMapper;
    /**
     * 获取app版本信息
     * @param softVersion 版本信息
     * @return 结果
     */
    @Override
    public List<SoftVersion> selectSoftVersion(SoftVersion softVersion) {
        return softVersionMapper.selectSoftVersion(softVersion);
    }

    /**
     * 通过id查询版本信息
     * @param id id
     * @return 结果
     */
    @Override
    public SoftVersion selectSoftVersionById(Integer id) {
        return softVersionMapper.selectSoftVersionById(id);
    }

    /**
     * 修改保存app版本
     * @param softVersion 版本信息
     * @return 结果
     */
    @Override
    public int updateVersion(SoftVersion softVersion) {
        return softVersionMapper.updateVersion(softVersion);
    }
}
