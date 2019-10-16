package com.ruoyi.project.app.domain;

import java.io.Serializable;

/**
 * app本地化
 * @Author: Rainey
 * @Date: 2019/10/14 17:13
 * @Version: 1.0
 **/
public class Language implements Serializable {
    private static final long serialVersionUID = 5419725483706796108L;
    private Integer id;
    private String info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", info='" + info + '\'' +
                '}';
    }
}
