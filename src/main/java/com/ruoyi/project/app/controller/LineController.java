package com.ruoyi.project.app.controller;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.app.domain.LineData;
import com.ruoyi.project.app.service.ILineService;
import com.ruoyi.project.code.activeCode.service.IActiveCodeService;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.service.IIsoService;
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.service.ISopService;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.service.ISopLineService;
import com.ruoyi.project.product.list.service.IDevProductListService;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.service.IWorkstationService;
import com.ruoyi.project.system.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app端产线交互控制层
 *
 * @author zqm
 */
@RestController
@RequestMapping("/app")
public class LineController {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(LineController.class);

    @Autowired
    private ILineService lineService;

    @Autowired
    private IWorkstationService workstationService;

    @Autowired
    private ISopService sopService;

    @Autowired
    private IDevProductListService productListService;

    @Autowired
    private ISopLineService sopLineService;

    @Autowired
    private IIsoService isoService;

    @Autowired
    private IActiveCodeService activeCodeService;


    @RequestMapping("/line")
    public AjaxResult lineAll() {
        try {
            return AjaxResult.success(lineService.selectAllLine());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 查询配置过的产品信息
     */
    @PostMapping("/searchCnfPro")
    public AjaxResult appSelectCnfPro(@RequestBody SopLine sopLine) {
        // 查询配置过的产品信息
        if (sopLine != null && sopLine.getLineId() != null) {
            sopLine.appStartPage();
            List<SopLine> sopLines = sopLineService.selectCnfPro(sopLine);
            return AjaxResult.success(sopLines);
        }
        return AjaxResult.error();
    }

    /******************************************************************************************************
     *********************************** app产线SOP交互 ***************************************************
     ******************************************************************************************************/
    /**
     * 查询配置过的sop
     */
    @PostMapping("/searchCnfSop")
    public AjaxResult appSelectCnfSop(@RequestBody Sop sop) {
        // 查询配置过的sop
        if (sop != null && sop.getLineId() != null) {
            sop.appStartPage();
            List<Iso> sops = sopService.selectCnfSop(sop);
            return AjaxResult.success(sops);
        }
        return AjaxResult.error();
    }

    /**
     * app端查询ASOP配置列表
     * 传入产线id
     */
    @PostMapping("/lineCnfSopList")
    public AjaxResult lineCnfSopList(@RequestBody Sop sop) {
        try {
            return AjaxResult.success("请求成功", sopService.selectSopList(sop));
        } catch (Exception e) {
            return AjaxResult.error("请求失败");
        }
    }


    /**
     * 新增产线SOP配置
     * 传入产线id
     */
    @PostMapping("/lineCnfSop")
    public AjaxResult lineCnfSop(@RequestBody Sop sop) {
        if (sop != null && sop.getLineId() != null) {
            Map<String, Object> map = new HashMap<>(16);
            // 查询该产线的所有工位信息
            map.put("stationList", workstationService.selectAllByLineId(sop.getLineId()));
            // 查询该产线所有未配置的产品信息
            map.put("proList", productListService.selectNotConfigByLineId2(sop.getLineId(), JwtUtil.getUser().getCompanyId(), FileConstants.SOP_TAG_LINE));
            // 查询所有的作业指导书
            map.put("isoList", isoService.selectAllIsoByPidAndCompanyId());
            return AjaxResult.success(map);
        }
        return AjaxResult.error();
    }

    /**
     * 产线跳转修改SOP配置
     * 传入sop主键id
     */
    @PostMapping("/lineSopDetail")
    public AjaxResult lineEditSop(@RequestBody Sop sop) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return AjaxResult.error("未登录或者登录超时");
        }
        if (sop != null && sop.getId() != null) {
            Map<String, Object> map = new HashMap<>(16);
            // 查看sop主表信息
            map.put("sop", sopService.selectSopById(sop.getId()));
            // 查询sop配置明细
            map.put("sopDetail", sopLineService.selectSopLineBySId(user.getCompanyId(), sop.getId()));
            return AjaxResult.success(map);
        }
        return AjaxResult.error();
    }

    /**
     * 查看工位的看板编码
     */
    @PostMapping("/selectWatchCode")
    public AjaxResult appSelectStationCode(@RequestBody Workstation workstation){
        if (workstation != null && workstation.getId() != null) {
            Workstation work = workstationService.selectWorkstationById(workstation.getId());
            if (work != null) {
                if (work.getcId() > 0) {
                    return AjaxResult.success(work.getcCode());
                } else {
                    return AjaxResult.error("没有配置看板编码");
                }
            }
            return AjaxResult.error("工作不存在或被删除");
        }
        return AjaxResult.error();
    }

    /**
     * 通过父id查询对应作业指导书下的页信息
     * 需传入 parentId 父id
     */
    @PostMapping("/selectIsoByPId")
    public AjaxResult selectIsoByPid(@RequestBody Iso iso) {
        if (iso != null && iso.getParentId() != null) {
            return AjaxResult.success(isoService.selectIsoByParentId(iso.getParentId()));
        }
        return AjaxResult.error();
    }


    /******************************************************************************************************
     *********************************** app产线工位交互 ***************************************************
     ******************************************************************************************************/

    /**
     * 工位配置看板硬件
     */
    @PostMapping("/lineCnfStation")
    public AjaxResult appLineCnfStation(@RequestBody Workstation workstation) {
        try {
            if (workstation != null && workstation.getId() != null) {
                int rows = workstationService.appLineCnfStation(workstation);
                return rows > 0 ? AjaxResult.success() : AjaxResult.error();
            }
        } catch (BusinessException e) {
            LOGGER.error("app工位配置看板硬件出现异常" + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.error();
    }

    /**
     * 产线工位关联配置
     */
    @RequestMapping("/lineConfigKbCode")
    public Map<String,Object> lineConfigKbCode(@RequestBody LineData lineData){
        return lineService.lineConfigKbCode(lineData);
    }
}
