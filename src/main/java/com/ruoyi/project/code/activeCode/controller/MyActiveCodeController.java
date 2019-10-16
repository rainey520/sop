package com.ruoyi.project.code.activeCode.controller;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import com.ruoyi.project.code.activeCode.service.IActiveCodeService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 激活码 信息操作处理
 *
 * @author sj
 * @date 2019-08-26
 */
@Controller
@RequestMapping("/code/myActiveCode")
public class MyActiveCodeController extends BaseController {
    private String prefix = "code/activeCode";

    @Autowired
    private IActiveCodeService activeCodeService;

    @RequiresPermissions("code:myActiveCode:view")
    @GetMapping()
    public String activeCode() {
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/myActiveCodeEn";
        }
        return prefix + "/myActiveCode";
    }

    /**
     * 查询激活码列表
     */
    @RequiresPermissions("code:myActiveCode:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActiveCode activeCode) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        activeCode.setCompanyId(user.getCompanyId());
        startPage();
        List<ActiveCode> list = activeCodeService.selectActiveCodeList(activeCode);
        return getDataTable(list);
    }


    /**
     * 导出激活码列表
     */
    @RequiresPermissions("code:myActiveCode:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActiveCode activeCode) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        activeCode.setCompanyId(user.getCompanyId());
        List<ActiveCode> list = activeCodeService.selectActiveCodeList(activeCode);
        ExcelUtil<ActiveCode> util = new ExcelUtil<ActiveCode>(ActiveCode.class);
        return util.exportExcel(list, "activeCode");
    }

    /**
     * 生成
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/{id}")
    public String rqCode(@PathVariable("id")int id,ModelMap modelMap){
        modelMap.put("d",activeCodeService.selectActiveCodeById(id));
        return prefix+"/rqcode";
    }
}
