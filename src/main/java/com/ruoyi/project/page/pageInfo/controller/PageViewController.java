package com.ruoyi.project.page.pageInfo.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.iso.iso.service.IIsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/c")
public class PageViewController {

    private String prefix = "tem";

    @Autowired
    private IIsoService iIsoService;

    /**
     * 作业指导书
     * @param code 硬件编码
     * @throws Exception
     */
    @RequestMapping("/{code}")
    public String  getSop(@PathVariable("code") String code, ModelMap mmap){
        mmap.put("code",code);
        try {
            mmap.put("data",iIsoService.selectSopByDevCode(code).get("iso"));
        }catch (Exception e){
            mmap.put("msg",e.getMessage());
        }
        return  prefix+"/sop";
    }

    /**
     * 获取数据
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("/d/{code}")
    public AjaxResult getSopData(@PathVariable("code")String code){
        try {
            return AjaxResult.success(iIsoService.selectSopByDevCode(code).get("iso"));
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取作业指导书
     * @param code 硬件编码
     * @return
     */
    @ResponseBody
    @RequestMapping("/s/{code}")
    public Map<String,Object> apiGetSop(@PathVariable("code")String code){
        Map<String,Object> map =new HashMap<>();
        try {
            map.put("code",1);
            map.put("data",iIsoService.selectSopByDevCode(code).get("data"));
            map.put("msg","请求成功");
        }catch (Exception e){
            map.put("code",0);
            map.put("data",null);
            map.put("msg",e.getMessage());
        }
        return map;
    }


}
