package com.ruoyi.project.system.config.mapper;

import com.ruoyi.project.app.domain.SoftVersion;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 通过id查询版本信息
     * @param id 版本id
     * @return 结果
     */
    SoftVersion selectSoftVersionById(@Param("id") Integer id);

    /**
     * 修改保存app版本
     * @param softVersion 版本信息
     * @return 结果
     */
    int updateVersion(SoftVersion softVersion);
}
