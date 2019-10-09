package com.ruoyi.project.app.service;

import com.ruoyi.project.app.domain.SoftVersion;

import java.util.List;

/**
 * 手机版本服务层接口
 * @Author: Rainey
 * @Date: 2019/10/9 15:08
 * @Version: 1.0
 **/
public interface ISoftVersionService {

    /**
     * 获取app版本信息
     * @param softVersion 版本信息
     * @return 结果
     */
    List<SoftVersion> selectSoftVersion(SoftVersion softVersion);

    /**
     * 通过id查询版本信息
     * @param id id
     * @return 结果
     */
    SoftVersion selectSoftVersionById(Integer id);

    /**
     * 修改保存app版本
     * @param softVersion 版本信息
     * @return 结果
     */
    int updateVersion(SoftVersion softVersion);
}
