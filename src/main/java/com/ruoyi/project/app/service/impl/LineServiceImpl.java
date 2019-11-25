package com.ruoyi.project.app.service.impl;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.app.domain.LineData;
import com.ruoyi.project.app.service.ILineService;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LineServiceImpl implements ILineService {

    @Autowired
    private ProductionLineMapper lineMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private DevListMapper devListMapper;

    @Override
    public List<ProductionLine> selectAllLine() {
        User user = JwtUtil.getUser();
        //查询对应公司所以的产线信息
        List<ProductionLine> lines = lineMapper.selectAllProductionLineByCompanyId(user.getCompanyId());
        if (lines != null) {
            for (ProductionLine line : lines) {
                //查询对应产线的工位信息
                line.setWorkstations(workstationMapper.selectAllCodeNotNullByLineId(user.getCompanyId(), line.getId()));
                line.setStatus(0);
                //查询正在进行的工单
                DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByCompandAndLine(user.getCompanyId(), line.getId(), WorkConstants.SING_LINE);
                if(workOrder != null){
                    line.setStatus(1);//生产中
                }
            }
            return lines;
        }
        return null;
    }

    /**
     * 工位看板编码关联配置
     * @param lineData 配置信息
     * @return 结果
     */
    @Override
    public Map<String, Object> lineConfigKbCode(LineData lineData) {
        Map<String,Object> map = new HashMap<>(16);
        User user = JwtUtil.getUser();
        if (user == null) {
            map.put("code",0);
            map.put("msg", UserConstants.NOT_LOGIN);
            return map;
        }
        if (lineData == null || StringUtils.isEmpty(lineData.getKbCode()) || lineData.getStationId() == null) {
            map.put("code",0);
            map.put("msg", "配置参数错误");
            return map;
        }
        // 查询新的硬件信息
        DevList newDev = devListMapper.selectDevListByDevId(lineData.getKbCode());
        if (newDev == null) {
            map.put("code",0);
            map.put("msg", "计数器硬件不存在");
            return map;
        }
        // 查询新看板编码之前配置的工位信息
        Workstation oldStation = workstationMapper.selectInfoByDevice(0, newDev.getId(), 0);
        if (oldStation != null) {
            if (oldStation.getId().equals(lineData.getStationId())) {
                map.put("code",1);
                map.put("msg","请求成功");
                return map;
            }
            if (DevConstants.DEV_CONFIRMTAG_NO.equals(lineData.getKbUpdateTag())) {
                map.put("code",2);
                map.put("msg", "该看板编码已配置过工位");
                return map;
            } else {
                // 设置之前工位为未配置状态
                oldStation.setcId(0);
                oldStation.setcCode(null);
                workstationMapper.updateWorkstationInfo(oldStation);
            }
        }
        // 查询现在配置的工位信息
        Workstation station = workstationMapper.selectWorkstationById(lineData.getStationId());
        if (station == null) {
            map.put("code",0);
            map.put("msg", "工位不存在或被删除");
            return map;
        }
        // 查询工位所在产线信息
        ProductionLine line = lineMapper.selectProductionLineById(station.getLineId());
        if (line == null) {
            map.put("code",0);
            map.put("msg", "产线不存在或被删除");
            return map;
        }
        // 说明配置过硬件
        if (StringUtils.isNotEmpty(station.getcCode())) {
            // 首次提示
            if (DevConstants.DEV_CONFIRMTAG_NO.equals(lineData.getKbUpdateTag())) {
                map.put("code",2);
                map.put("msg", "该工位已经关联过看板编码");
                return map;
            // 更新之前的硬件信息
            } else {
                devListMapper.updateDevSignAndType1(user.getCompanyId(),station.getcId(),DevConstants.DEV_SIGN_NOT_USE,null,null);
            }
        }
        // 更新硬件信息
        newDev.setDevType(DevConstants.DEV_TYPE_LINE);
        newDev.setSign(DevConstants.DEV_SIGN_USED);
        newDev.setCompanyId(user.getCompanyId());
        newDev.setDeviceName(line.getLineName() + "--" + station.getwName());
        devListMapper.updateDevList(newDev);
        // 更新工位信息
        station.setcId(newDev.getId());
        station.setcCode(newDev.getDeviceId());
        workstationMapper.updateWorkstationInfo(station);
        map.put("code",1);
        map.put("msg","请求成功");
        return map;
    }
}
