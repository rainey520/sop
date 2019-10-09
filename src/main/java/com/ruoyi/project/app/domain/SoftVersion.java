package com.ruoyi.project.app.domain;

import java.io.Serializable;

/**
 * 手机软件实体
 * @Author: Rainey
 * @Date: 2019/10/9 15:09
 * @Version: 1.0
 **/
public class SoftVersion implements Serializable {
    private static final long serialVersionUID = -5722331232482041213L;

    private Integer id;
    private String type;
    private String version;
    /**
     * 类型(1、ASOP，2、生产看板，3、用户APP端)
     */
    private Integer tag;

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SoftVersion{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", version='" + version + '\'' +
                ", tag=" + tag +
                '}';
    }
}
