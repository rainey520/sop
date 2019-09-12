package com.ruoyi.project.code.activeCode.domain;

import java.io.Serializable;

/**
 * 激活码激活类
 */
public class ApiActiveCode implements Serializable {
    private String code;
    private String imei;
    private String watchCode;

    public String getWatchCode() {
        return watchCode;
    }

    public void setWatchCode(String watchCode) {
        this.watchCode = watchCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "ApiActiveCode{" +
                "code='" + code + '\'' +
                ", imei='" + imei + '\'' +
                ", watchCode='" + watchCode + '\'' +
                '}';
    }
}
