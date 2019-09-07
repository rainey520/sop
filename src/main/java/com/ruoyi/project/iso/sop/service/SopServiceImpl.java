package com.ruoyi.project.iso.sop.service;

import java.util.Collections;
import java.util.List;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.iso.sop.mapper.SopMapper;
import com.ruoyi.project.iso.sop.domain.Sop;
import com.ruoyi.project.iso.sop.service.ISopService;
import com.ruoyi.common.support.Convert;

/**
 * SOP配置主 服务层实现
 * 
 * @author sj
 * @date 2019-08-30
 */
@Service
public class SopServiceImpl implements ISopService 
{
	@Autowired
	private SopMapper sopMapper;

	@Autowired
	private IsoMapper isoMapper;

	/**
     * 查询SOP配置主信息
     * 
     * @param id SOP配置主ID
     * @return SOP配置主信息
     */
    @Override
	public Sop selectSopById(Integer id)
	{
	    return sopMapper.selectSopById(id);
	}
	
	/**
     * 查询SOP配置主列表
     * 
     * @param sop SOP配置主信息
     * @return SOP配置主集合
     */
	@Override
	public List<Sop> selectSopList(Sop sop)
	{
		User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
		sop.setCompanyId(user.getCompanyId());
	    return sopMapper.selectSopList(sop);
	}
	
    /**
     * 新增SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	@Override
	public int insertSop(Sop sop)
	{
	    return sopMapper.insertSop(sop);
	}
	
	/**
     * 修改SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	@Override
	public int updateSop(Sop sop)
	{
	    return sopMapper.updateSop(sop);
	}

	/**
     * 删除SOP配置主对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteSopByIds(String ids)
	{
		return sopMapper.deleteSopByIds(Convert.toStrArray(ids));
	}

	/**
	 * 通过作业指导书id查询已配置列表
	 * @param companyId 公司id
	 * @param isoId 作业指导书id
	 * @return 结果
	 */
	@Override
	public List<Sop> selectSopListBySopId(Integer companyId, Integer isoId) {
		return sopMapper.selectSopListBySopId(companyId,isoId);
	}

	/**
	 * 查询配置过的作业指导书
	 * @param sop 配置信息
	 * @return 结果
	 */
	@Override
	public List<Iso> selectCnfSop(Sop sop) {
		User user = JwtUtil.getUser();
		if (user == null) {
		    return Collections.emptyList();
		}
		sop.setCompanyId(user.getCompanyId());
		return isoMapper.selectCnfSop(sop);
	}
}
