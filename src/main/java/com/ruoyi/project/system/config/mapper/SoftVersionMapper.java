package com.ruoyi.project.system.config.mapper;

import com.ruoyi.project.app.domain.SoftVersion;

import java.util.List;

/**
 * 手机版本数据层
 * @Author: Rainey
 * @Date: 2019/10/9 15:07
 * @Version: 1.0
 **/
public interface SoftVersionMapper {

    /**
     * 获取app版本
     * @param softVersion 版本信息
     * @return 结果
     */
    List<SoftVersion> selectSoftVersion(SoftVersion softVersion);
}
