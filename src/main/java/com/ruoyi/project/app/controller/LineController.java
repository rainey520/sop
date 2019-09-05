package com.ruoyi.project.app.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.app.service.ILineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class LineController {

    @Autowired
    private ILineService lineService ;

    @RequestMapping("/line")
    public AjaxResult lineAll(){
        try {
            return AjaxResult.success(lineService.selectAllLine());
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
}
