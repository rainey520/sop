package com.ruoyi.project.code.activeCode.controller;

import com.ruoyi.project.code.activeCode.domain.ApiActiveCode;
import com.ruoyi.project.code.activeCode.service.IActiveCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 激活码激活
 */
@RestController
public class ApiActiveCodeController {

    @Autowired
    private IActiveCodeService activeCodeService;

    /**
     * 激活码进行激活
     * @param code 传人 激活码和手机的IMEI号
     * @return
     */
    @RequestMapping("/a/c")
    public Map<String,Object> activeCode(@RequestBody ApiActiveCode code){
        return activeCodeService.activeCode(code);

    }
}
