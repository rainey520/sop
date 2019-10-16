package com.ruoyi.project.iso.sopLine.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.service.ISopService;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.project.product.list.service.IDevProductListService;
import com.ruoyi.project.production.productionLine.service.IProductionLineService;
import com.ruoyi.project.production.workstation.service.IWorkstationService;
import com.ruoyi.project.system.user.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 作业指导书  产线 配置 信息操作处理
 *
 * @author sj
 * @date 2019-06-15
 */
@Controller
@RequestMapping("/iso/sopLine")
public class SopLineController extends BaseController {
    private String prefix = "iso/sopLine";

    @Autowired
    private ISopLineService sopLineService;

    @Autowired
    private IIsoService iIsoService;

    @Autowired
    private IDevProductListService productListService;

    @Autowired
    private IWorkstationService workstationService;

    @Autowired
    private IProductionLineService lineService;

    @Autowired
    private ISopService iSopService;


    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/view/{id}")
    public String sopLine(@PathVariable("id") Integer isoId, ModelMap mmap) {
        mmap.put("isoId", isoId);
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/sopLineEn";
        }
        return prefix + "/sopLine";
    }

    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/showDetail/{id}")
    public String showdetail(@PathVariable("id") Integer isoId, ModelMap mmap) {
        mmap.put("isoId", isoId);
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/sopLineDetailEn";
        }
        return prefix + "/sopLineDetail";
    }

    /**
     * 查询作业指导书  产线 配置列表
     */
    @RequiresPermissions("iso:sopLine:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Sop sop) {
        startPage();
        List<Sop> list = iSopService.selectSopList(sop);
        return getDataTable(list);
    }

    /**
     * 新增作业指导书  产线 配置
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") Integer isoId, ModelMap modelMap, HttpServletRequest request) {
        User user = JwtUtil.getTokenUser(request);
        // 查询未配置该作业指导书的所有产线信息
        modelMap.put("lineList", lineService.selectLineNotConfigByIsoId(isoId, user.getCompanyId()));
        modelMap.put("pages", iIsoService.selectIsoByParentId(isoId));
        modelMap.put("isoId", isoId);
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/addEn";
        }
        return prefix + "/add";
    }

    /**
     * 新增保存作业指导书  产线 配置
     */
    @RequiresPermissions("iso:sopLine:add")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody Sop sop) {
        try {
            return toAjax(sopLineService.insertSopLine(sop));
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 修改作业指导书  产线 配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap mmap) {
        Sop sop = iSopService.selectSopById(id);
        mmap.put("sop", sop);
        // 查询所有产线信息
        mmap.put("lineList", lineService.selectLineNotConfigByIsoId(sop.getSopId(), sop.getCompanyId()));
        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId(sop.getLineId(), sop.getCompanyId(), FileConstants.SOP_TAG_LINE, sop.getSopId()));
        //根据sop 产线 配置查询已经配置的产品信息
        mmap.put("cnfPns", productListService.selectSopConfigBySid(sop.getId(), sop.getCompanyId()));
        //        //查询对应产线的所以工位信息
        mmap.put("work", workstationService.selectAllByLineId(sop.getLineId()));
        mmap.put("pages", iIsoService.selectIsoByParentId(sop.getSopId()));
        //查询所有工位配置信息
        mmap.put("sopLineWork", sopLineService.selectWorkstionBySId(sop.getId()));
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/editEn";
        }
        return prefix + "/edit";
    }

    /**
     * 修改保存作业指导书  产线 配置
     */
    @RequiresPermissions("iso:sopLine:edit")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody Sop sop) {
        try {
            return toAjax(sopLineService.updateSopLine(sop));
        } catch (BusinessException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            return error(e.getMessage());
        }

    }

    /**
     * 删除作业指导书  产线 配置
     */
    @RequiresPermissions("iso:sopLine:remove")
    @Log(title = "作业指导书  产线 配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(int id) {
        return toAjax(sopLineService.deleteSopLine(id));
    }

    /******************    产线SOP 配置 *************************/

    @RequiresPermissions("iso:sopLine:list")
    @GetMapping("/config/{id}")
    public String sopLineConfig(@PathVariable("id") int id, ModelMap mmap) {
        mmap.put("line", id);
        //查询所以的SOP 作业指导书
        mmap.put("sops", iIsoService.selectIsoByParentId(FileConstants.FOLDER_SOP));
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/sopLineConfigEn";
        }
        return prefix + "/sopLineConfig";
    }


    /**
     * 新增作业指导书  产线 配置
     */
    @GetMapping("/addConfig")
    @RequiresPermissions("iso:sopLine:add")
    public String addConfig(int lineId, ModelMap mmap, HttpServletRequest request) {
        //查询该产线所有未配置的SOP书
        mmap.put("iso", iIsoService.selectAllIsoByPidAndCompanyId());
        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId2(lineId, JwtUtil.getTokenUser(request).getCompanyId(), FileConstants.SOP_TAG_LINE));
        //查询对应产线的所以工位信息
        mmap.put("work", workstationService.selectAllByLineId(lineId));
        mmap.put("line", lineId);
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/add1En";
        }
        return prefix + "/add1";
    }

    /**
     * 修改作业指导书 产线 配置
     *
     * @param id SOP id
     * @return
     */
    @GetMapping("/editConfig/{id}")
    @RequiresPermissions("iso:sopLine:edit")
    public String editConfig(@PathVariable("id") int id, ModelMap mmap) {
        Sop sop = iSopService.selectSopById(id);
        mmap.put("sop", sop);
//        //查询该产线所有未配置的SOP书
        mmap.put("iso", iIsoService.selectAllIsoByPidAndCompanyId());
//        //根据产线id查询所以未配置的产品信息
        mmap.put("pns", productListService.selectNotConfigByLineId2(sop.getLineId(), sop.getCompanyId(), FileConstants.SOP_TAG_LINE));
        //根据sop 产线 配置查询已经配置的产品信息
        mmap.put("cnfPns", productListService.selectSopConfigBySid(sop.getId(), sop.getCompanyId()));
//      查询对应产线的所以工位信息
        mmap.put("work", workstationService.selectAllByLineId(sop.getLineId()));
        //查询对应的指导书的页数
        mmap.put("pages", iIsoService.selectIsoByParentId(sop.getSopId()));
        //查询所有工位配置信息
        mmap.put("sopLineWork", sopLineService.selectWorkstionBySId(sop.getId()));
        User user = JwtUtil.getUser();
        if (UserConstants.LANGUAGE_EN.equals(user.getLangVersion())) {
            return prefix + "/edit1En";
        }
        return prefix + "/edit1";
    }



}
