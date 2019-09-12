package com.ruoyi.project.code.activeCode.mapper;

import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 激活码 数据层
 * 
 * @author sj
 * @date 2019-08-26
 */
public interface ActiveCodeMapper 
{
	/**
     * 查询激活码信息
     * 
     * @param id 激活码ID
     * @return 激活码信息
     */
	public ActiveCode selectActiveCodeById(Integer id);

	/**
	 * 根据激活码查询对应激活码是否存在
	 * @param code 激活码
	 * @return
	 */
	public ActiveCode selectActiveCodeByCode(@Param("code")	String code);

	/**
	 * 根据IMEI 号查询对应的设备是否已经激活
	 * @param imei
	 * @return
	 */
	public ActiveCode selectActiveCodeByImei(@Param("imei")String imei);
	
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
     * @param activeCode 激活码信息
     * @return 结果
     */
	public int insertActiveCode(ActiveCode activeCode);
	
	/**
     * 修改激活码
     * 
     * @param activeCode 激活码信息
     * @return 结果
     */
	public int updateActiveCode(ActiveCode activeCode);

	/**
	 * 根据激活查询对应的激活码信息
	 * @param code 激活码
	 * @return
	 */
	ActiveCode selctActiveCodeByCode(@Param("code")String code);

	/**
	 * 通过激活码硬件编码查询激活码信息
	 * @param code 硬件编码
	 * @param activeCode 激活码
	 * @return
	 */
	ActiveCode selectUniqueActiveCode(@Param("code") String code,@Param("activeCode") String activeCode);
}