package com.ruoyi.project.app.service;

import com.ruoyi.project.app.domain.Language;

/**
 * app本地化接口
 * @Author: Rainey
 * @Date: 2019/10/14 17:22
 * @Version: 1.0
 **/
public interface ILanguageService {

    /**
     * 查看app本地信息
     * @return 结果
     */
    Language selectInfo();
}
