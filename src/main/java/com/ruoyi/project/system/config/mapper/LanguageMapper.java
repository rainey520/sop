package com.ruoyi.project.system.config.mapper;

import com.ruoyi.project.app.domain.Language;

/**
 * @Author: Rainey
 * @Date: 2019/10/14 17:13
 * @Version: 1.0
 **/
public interface LanguageMapper {

    /**
     * 查找app本地化信息
     * @return 结果
     */
    public Language selectLanguage();
}
