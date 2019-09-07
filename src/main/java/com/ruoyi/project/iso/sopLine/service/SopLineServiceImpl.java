package com.ruoyi.project.iso.sopLine.service;

import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.mapper.SopMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.domain.SopLineWork;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.iso.sopLine.mapper.SopLineWorkMapper;
import com.ruoyi.project.product.list.domain.DevProductList;
import com.ruoyi.project.product.list.mapper.DevProductListMapper;
import com.ruoyi.project.production.singleWork.mapper.SingleWorkMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.CollationElementIterator;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 作业指导书  产线 配置 服务层实现
 *
 * @author sj
 * @date 2019-06-15
 */
@Service
public class SopLineServiceImpl implements ISopLineService {
    @Autowired
    private SopLineMapper sopLineMapper;

    @Autowired
    private DevProductListMapper productListMapper;

    @Autowired
    private SopLineWorkMapper sopLineWorkMapper;

    @Autowired
    private IsoMapper isoMapper;

    @Autowired
    private SopMapper sopMapper;

    /**
     * 查询作业指导书  产线 配置信息
     *
     * @param id 作业指导书  产线 配置ID
     * @return 作业指导书  产线 配置信息
     */
    @Override
    public SopLine selectSopLineById(Integer id) {
        return sopLineMapper.selectSopLineById(id);
    }

    /**
     * 查询作业指导书  产线 配置列表
     *
     * @param sopLine 作业指导书  产线 配置信息
     * @return 作业指导书  产线 配置集合
     */
    @Override
    public List<SopLine> selectSopLineList(SopLine sopLine) {
        return sopLineMapper.selectSopLineList(sopLine);
    }

    /**
     * 新增作业指导书  产线 配置
     *
     * @param Sop 作业指导书  产线 配置信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSopLine(Sop sop) throws Exception {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        if (sop.getPns() == null || sop.getPns().length <= 0 ||
                sop.getSopLines() == null || sop.getSopLines().size() <= 0) {
            throw new BusinessException("配置异常，请联系管理员");
        }
        Sop s = sopMapper.selectSopByCnfName(user.getCompanyId(), sop.getCnfName());
        if (s != null) {
            throw new BusinessException("配置名称已经存在");
        }
        //查询对应产品信息
        StringBuilder pCodes = new StringBuilder();
        DevProductList productList = null;
        for (int p : sop.getPns()) {
            productList = productListMapper.selectDevProductListById(p);
            if (productList == null) {
                continue;
            }
            pCodes.append(productList.getProductCode() + ",");
        }
        if (pCodes.toString().length() <= 0) {
            throw new BusinessException("配置产品编码异常");
        }
        sop.setpId(StringUtils.join(sop.getPns(), ","));
        sop.setpCode(pCodes.toString().substring(0, pCodes.lastIndexOf(",")));
        sop.setCompanyId(user.getCompanyId());
        sop.setCreateTime(new Date());
        sopMapper.insertSop(sop);
        handleSopDetailConfig(sop, user);
        return 1;
    }

    /**
     * 修改作业指导书  产线 配置
     *
     * @param sop 作业指导书  产线 配置信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSopLine(Sop sop) throws Exception {
        User user = JwtUtil.getUser();
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        if (sop.getPns() == null || sop.getPns().length <= 0 ||
                sop.getSopLines() == null || sop.getSopLines().size() <= 0) {
            throw new BusinessException("配置异常，请联系管理员");
        }
        Sop s = sopMapper.selectSopByCnfName(user.getCompanyId(), sop.getCnfName());
        if (s != null && s.getId() != sop.getId()) {
            throw new BusinessException("配置名称已经存在");
        }
        //删除对应详情配置信息
        sopLineMapper.deleteSopLineBySid(sop.getId());
        //修改sop配置主信息
        StringBuilder pCodes = new StringBuilder();
        DevProductList productList = null;
        for (int p : sop.getPns()) {
            productList = productListMapper.selectDevProductListById(p);
            if (productList == null) {
                continue;
            }
            pCodes.append(productList.getProductCode() + ",");
        }
        if (pCodes.toString().length() <= 0) {
            throw new BusinessException("配置产品编码异常");
        }
        sop.setpId(StringUtils.join(sop.getPns(), ","));
        sop.setpCode(pCodes.toString().substring(0, pCodes.lastIndexOf(",")));
        sopMapper.updateSop(sop);
        handleSopDetailConfig(sop, user);
        return 1;
    }

    private void handleSopDetailConfig(Sop sop, User user) throws Exception {
        DevProductList productList = null;
        for (SopLine sopLine : sop.getSopLines()) {
            if (sopLine.getPageId() == null || sopLine.getPageId() <= 0) {
                continue;
            }
            for (int p : sop.getPns()) {
                productList = productListMapper.selectDevProductListById(p);
                if (productList == null) {
                    continue;
                }
                sopLine.setCompanyId(user.getCompanyId());
                sopLine.setcId(user.getUserId().intValue());
                sopLine.setcTime(new Date());
                sopLine.setPnId(p);
                sopLine.setPnCode(productList.getProductCode());
                sopLine.setSopTag(0);
                sopLine.setSid(sop.getId());
                sopLineMapper.insertSopLine(sopLine);
            }

        }
    }

    /**
     * 删除作业指导书  产线 配置对象
     *
     * @param id 主表id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSopLine(int id) {
        try {
            sopLineMapper.deleteSopLineBySid(id);
            sopMapper.deleteSopById(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据公司id 产线id SOP id查询所以的产线SOP 配置细心
     *
     * @param companyId 公司id
     * @param lineId    产线id
     * @param sopId     SOP id
     * @return
     */
    @Override
    public List<SopLine> selectLineAllSopConfig(int companyId, int lineId, int sopId, int sopTag) {
        return sopLineMapper.selectLineAllSopConfig(companyId, lineId, sopId, sopTag);
    }

    /**
     * 查询所以的工位指导书页配置信息
     *
     * @param sid 主表id
     * @return
     */
    @Override
    public List<SopLineWork> selectWorkstionBySId(int sid) {
        return sopLineWorkMapper.selectWorkstionBySId(sid);
    }

    /**
     * 查询作业指导书产线配置列表
     *
     * @param isoId     作业指导书id
     * @param companyId 公司id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineListBySopId(Integer companyId, Integer isoId) {
        return sopLineMapper.selectSopLineListBySopId(companyId, isoId);
    }

    /**
     * 查询作业指导书工位配置列表
     *
     * @param companyId 公司id
     * @param isoId     作业指导书id
     * @return 结果
     */
    @Override
    public List<SopLineWork> selectSopLineWorkListBySopId(Integer companyId, Integer isoId) {
        return sopLineWorkMapper.selectSopLineWorkListBySopId(companyId, isoId);
    }


    /******************************  单工位SOP配置 ***********************************************/

    @Autowired
    private SingleWorkMapper singleWorkMapper;

    /**
     * 查询单工位SOP配置列表
     *
     * @param sopLine sop信息
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineList2(SopLine sopLine) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        sopLine.setCompanyId(user.getCompanyId());
        List<SopLine> sopLines = sopLineMapper.selectSopLineList2(sopLine);
        for (SopLine line : sopLines) {
            Integer imId = singleWorkMapper.selectSingleWorkById(line.getLineId()).getImId();
//            String imCode = instrumentManageMapper.selectInstrumentManageById(imId).getImCode();
            String imCode = null;
            line.setImName(imCode);
        }
        return sopLines;
    }

    /**
     * 查询单工位的配置信息
     *
     * @param companyId 公司id
     * @param lineId    车间id
     * @param sopId     sopid
     * @param wId       工位id
     * @param sopTag    sop生产配置标记
     * @return
     */
    @Override
    public SopLineWork selectSopLineWorkInfo(int companyId, int lineId, int sopId, int wId, int sopTag) {
        SopLineWork sopLineWork = sopLineWorkMapper.selectInfoByApi(companyId, lineId, sopId, wId, sopTag);
        if (StringUtils.isNotNull(sopLineWork)) {
            sopLineWork.setcName(isoMapper.selectIsoById(sopLineWork.getPageId()).getcName());
        }
        return sopLineWork;
    }

    /**
     * 通过页信息查询配置列表
     *
     * @param companyId 公司id
     * @param pageId    页id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineListByPageId(Integer companyId, Integer pageId) {
        return sopLineMapper.selectSopLineListByPageId(companyId, pageId);
    }

    /**
     * 查询配置过的产品
     * @param sopLine 配置信息
     * @return 结果
     */
    @Override
    public List<SopLine> selectCnfPro(SopLine sopLine) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        sopLine.setCompanyId(user.getCompanyId());
        return sopLineMapper.selectCnfPro(sopLine);
    }

    /**
     * app端查询sop配置明细
     * @param companyId 公司id
     * @param sId sop主表id
     * @return 结果
     */
    @Override
    public List<SopLine> selectSopLineBySId(Integer companyId, Integer sId) {
        return sopLineMapper.selectSopLineBySId(companyId,sId);
    }
}
