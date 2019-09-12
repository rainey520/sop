package com.ruoyi.project.code.activeCode.service;

import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import com.ruoyi.project.code.activeCode.domain.ApiActiveCode;

import java.util.List;
import java.util.Map;

/**
 * 激活码 服务层
 * 
 * @author sj
 * @date 2019-08-26
 */
public interface IActiveCodeService 
{
	/**
     * 查询激活码信息
     * 
     * @param id 激活码ID
     * @return 激活码信息
     */
	public ActiveCode selectActiveCodeById(Integer id);
	
	/**
     * 查询激活码列表
     * 
     * @param activeCode 激活码信息
     * @return 激活码集合
     */
	public List<ActiveCode> selectActiveCodeList(ActiveCode activeCode);
	
	/**
     * 新增激活码
     * 
     * @param codeNumber 需要生成的个数
     * @return 结果
     */
	public int insertActiveCode(int codeNumber) throws Exception;

	/**
	 * 给注册公司分配激活码
	 * @param ids 激活码id
	 * @param companyId 公司id
	 * @return
	 */
	public int updateActiveCode(String ids ,Integer companyId) throws Exception;


	/**
	 * 激活码激活
	 * @param code
	 * @return
	 */
	Map<String,Object> activeCode(ApiActiveCode code);

	/**
	 * 硬件和激活码绑定
	 * @param code 绑定信息
	 * @return 结果
	 */
	Map<String, Object> relationDevCode(ApiActiveCode code);

	/**
	 * 通过看板编码查询关联的激活码信息
	 * @param watchCode 看板硬件编码
	 * @return 结果
	 */
	ActiveCode selectActiveCodeByImei(String watchCode);

	/**
	 * 更换激活码
	 * @param code 绑定信息
	 * @return 结果
	 */
	Map<String, Object> changeActiveCode(ApiActiveCode code);
}
