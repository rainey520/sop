package com.ruoyi.project.app.service;

import com.ruoyi.project.app.domain.LineData;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;

import java.util.List;
import java.util.Map;

/**
 * 查询工位操作
 */
public interface ILineService {
    /**
     * 查询所以产线，及其工位信息
     * @return
     */
    List<ProductionLine> selectAllLine();

    /**
     * 看板编码工位关联配置
     * @param lineData 配置信息
     * @return 结果
     */
    Map<String, Object> lineConfigKbCode(LineData lineData);
}
