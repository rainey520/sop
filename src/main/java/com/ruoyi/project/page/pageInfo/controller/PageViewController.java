package com.ruoyi.project.page.pageInfo.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.code.activeCode.domain.ApiActiveCode;
import com.ruoyi.project.iso.iso.service.IIsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/c")
public class PageViewController {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageViewController.class);

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
            // mmap.put("data",iIsoService.selectSopByDevCode(code,null).get("iso"));
            mmap.put("msg","失败");
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
            return AjaxResult.error("失败");
            // return AjaxResult.success(iIsoService.selectSopByDevCode(code,null).get("iso"));
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取作业指导书
     * @param activeCode 请求信息
     * @return 结果
     */
    @ResponseBody
    @PostMapping("/app/watch")
    public Map<String,Object> apiGetSop(@RequestBody ApiActiveCode activeCode){
        return iIsoService.selectSopByDevCode(activeCode);
    }


}
