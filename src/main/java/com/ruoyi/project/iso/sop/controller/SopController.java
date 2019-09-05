package com.ruoyi.project.iso.sop.controller;

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
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.service.ISopService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * SOP配置主 信息操作处理
 * 
 * @author sj
 * @date 2019-08-30
 */
@Controller
@RequestMapping("/iso/sop")
public class SopController extends BaseController
{
    private String prefix = "iso/sop";
	
	@Autowired
	private ISopService sopService;
	
	@RequiresPermissions("iso:sop:view")
	@GetMapping()
	public String sop()
	{
	    return prefix + "/sop";
	}
	
	/**
	 * 查询SOP配置主列表
	 */
	@RequiresPermissions("iso:sop:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Sop sop)
	{
		startPage();
        List<Sop> list = sopService.selectSopList(sop);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出SOP配置主列表
	 */
	@RequiresPermissions("iso:sop:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Sop sop)
    {
    	List<Sop> list = sopService.selectSopList(sop);
        ExcelUtil<Sop> util = new ExcelUtil<Sop>(Sop.class);
        return util.exportExcel(list, "sop");
    }
	
	/**
	 * 新增SOP配置主
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存SOP配置主
	 */
	@RequiresPermissions("iso:sop:add")
	@Log(title = "SOP配置主", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Sop sop)
	{		
		return toAjax(sopService.insertSop(sop));
	}

	/**
	 * 修改SOP配置主
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Sop sop = sopService.selectSopById(id);
		mmap.put("sop", sop);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存SOP配置主
	 */
	@RequiresPermissions("iso:sop:edit")
	@Log(title = "SOP配置主", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Sop sop)
	{		
		return toAjax(sopService.updateSop(sop));
	}
	
	/**
	 * 删除SOP配置主
	 */
	@RequiresPermissions("iso:sop:remove")
	@Log(title = "SOP配置主", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(sopService.deleteSopByIds(ids));
	}
	
}
