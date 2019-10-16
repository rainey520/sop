package com.ruoyi.project.app.service.impl;

import com.ruoyi.project.app.domain.Language;
import com.ruoyi.project.app.service.ILanguageService;
import com.ruoyi.project.system.config.mapper.LanguageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * app本地化服务层
 * @Author: Rainey
 * @Date: 2019/10/14 17:23
 * @Version: 1.0
 **/
@Service
public class LanguageServiceImpl implements ILanguageService {

    @Autowired
    private LanguageMapper languageMapper;

    /**
     * app查看本地化
     * @return 结果
     */
    @Override
    public Language selectInfo() {
        return languageMapper.selectLanguage();
    }
}
