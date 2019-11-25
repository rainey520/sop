package com.ruoyi.project.app.domain;

import java.io.Serializable;

/**
 * 产线数据
 * @Author: Rainey
 * @Date: 2019/10/28 18:17
 * @Version: 1.0
 **/
public class LineData implements Serializable {
    private static final long serialVersionUID = -1700027777755792063L;
    /** 看板编码 */
    private String kbCode;
    /** 工位id */
    private Integer stationId;
    /** 看板更新确认标记，0、未确认，1、确认修改 */
    private Integer kbUpdateTag;

    public String getKbCode() {
        return kbCode;
    }

    public void setKbCode(String kbCode) {
        this.kbCode = kbCode;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getKbUpdateTag() {
        return kbUpdateTag;
    }

    public void setKbUpdateTag(Integer kbUpdateTag) {
        this.kbUpdateTag = kbUpdateTag;
    }

    @Override
    public String toString() {
        return "LineData{" +
                "kbCode='" + kbCode + '\'' +
                ", stationId=" + stationId +
                ", kbUpdateTag=" + kbUpdateTag +
                '}';
    }
}
