package com.ruoyi.project.iso.sop.mapper;

import com.ruoyi.project.iso.sop.domain.Sop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SOP配置主 数据层
 * 
 * @author sj
 * @date 2019-08-30
 */
public interface SopMapper 
{
	/**
     * 查询SOP配置主信息
     * 
     * @param id SOP配置主ID
     * @return SOP配置主信息
     */
	public Sop selectSopById(Integer id);
	
	/**
     * 查询SOP配置主列表
     * 
     * @param sop SOP配置主信息
     * @return SOP配置主集合
     */
	public List<Sop> selectSopList(Sop sop);

	/**
	 * 根据公司id 配置名称查询对应的信息是否存在
	 * @param companyId 公司id
	 * @param cnfName 配置名称
	 * @return
	 */
	Sop selectSopByCnfName(@Param("companyId")int companyId,@Param("cnfName")String cnfName);
	
	/**
     * 新增SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	public int insertSop(Sop sop);
	
	/**
     * 修改SOP配置主
     * 
     * @param sop SOP配置主信息
     * @return 结果
     */
	public int updateSop(Sop sop);
	
	/**
     * 删除SOP配置主
     * 
     * @param id SOP配置主ID
     * @return 结果
     */
	public int deleteSopById(Integer id);
	
	/**
     * 批量删除SOP配置主
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSopByIds(String[] ids);

	/**
	 * 通过产线id删除sop主表信息
	 * @param companyId 公司id
	 * @param lineId 产线id
	 * @return 结果
	 */
    int deleteSopByLineId(@Param("companyId") Integer companyId,@Param("lineId") Integer lineId);

	/**
	 * 通过作业指导书id查询配置列表
	 * @param companyId 结果
	 * @param sopId 作业指导书id
	 * @return 结果
	 */
    List<Sop> selectSopListBySopId(@Param("companyId") Integer companyId, @Param("sopId") Integer sopId);

}