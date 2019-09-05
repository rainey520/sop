package com.ruoyi.project.code.activeCode.domain;

/**
 * 激活码激活类
 */
public class ApiActiveCode {
    private String code;
    private String imei;

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
                '}';
    }
}
