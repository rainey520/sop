package com.ruoyi.project.code.activeCode.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import com.ruoyi.project.code.activeCode.service.IActiveCodeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 激活码 信息操作处理
 *
 * @author sj
 * @date 2019-08-26
 */
@Controller
@RequestMapping("/code/activeCode")
public class ActiveCodeController extends BaseController {
    private String prefix = "code/activeCode";

    @Autowired
    private IActiveCodeService activeCodeService;

    @RequiresPermissions("code:activeCode:view")
    @GetMapping()
    public String activeCode() {
        return prefix + "/activeCode";
    }

    /**
     * 查询激活码列表
     */
    @RequiresPermissions("code:activeCode:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActiveCode activeCode) {
        startPage();
        List<ActiveCode> list = activeCodeService.selectActiveCodeList(activeCode);
        return getDataTable(list);
    }


    /**
     * 导出激活码列表
     */
    @RequiresPermissions("code:activeCode:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActiveCode activeCode) {
        List<ActiveCode> list = activeCodeService.selectActiveCodeList(activeCode);
        ExcelUtil<ActiveCode> util = new ExcelUtil<ActiveCode>(ActiveCode.class);
        return util.exportExcel(list, "activeCode");
    }

    /**
     * 新增激活码
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存激活码
     */
    @RequiresPermissions("code:activeCode:add")
    @Log(title = "激活码", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(int codeNumber) {
        try {
            activeCodeService.insertActiveCode(codeNumber);
            return success();
        } catch (Exception e) {
            return error(e.getMessage());
        }

    }

    /**
     * 激活码分配
     */
    @GetMapping("/edit/{ids}")
    public String edit(@PathVariable("ids") String ids, ModelMap mmap) {
        mmap.put("ids", ids);
        return prefix + "/edit";
    }

    /**
     * 给注册公司分配激活码
     */
    @RequiresPermissions("code:activeCode:edit")
    @Log(title = "分配激活码", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(String ids, Integer companyId) {
        try {
             activeCodeService.updateActiveCode(ids,companyId);
            return success();
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }


}
