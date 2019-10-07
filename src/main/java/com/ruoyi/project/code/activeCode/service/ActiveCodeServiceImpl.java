package com.ruoyi.project.code.activeCode.service;

import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import com.ruoyi.project.code.activeCode.domain.ApiActiveCode;
import com.ruoyi.project.code.activeCode.mapper.ActiveCodeMapper;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 激活码 服务层实现
 * 
 * @author sj
 * @date 2019-08-26
 */
@Service
public class ActiveCodeServiceImpl implements IActiveCodeService 
{
	@Autowired
	private ActiveCodeMapper activeCodeMapper;

	@Autowired
	private DevCompanyMapper devCompanyMapper;

	@Autowired
	private DevListMapper devListMapper;

	/**
     * 查询激活码信息
     * 
     * @param id 激活码ID
     * @return 激活码信息
     */
    @Override
	public ActiveCode selectActiveCodeById(Integer id)
	{
	    return activeCodeMapper.selectActiveCodeById(id);
	}
	
	/**
     * 查询激活码列表
     * 
     * @param activeCode 激活码信息
     * @return 激活码集合
     */
	@Override
	public List<ActiveCode> selectActiveCodeList(ActiveCode activeCode)
	{
	    return activeCodeMapper.selectActiveCodeList(activeCode);
	}
	
    /**
     * 新增激活码
     * 
     * @param codeNumber 需要生成的个数
     * @return 结果
     */
	@Override
	public int insertActiveCode(int codeNumber) throws Exception
	{
		if(codeNumber == 0){
			throw  new Exception("生成激活码个数必须大于0");
		}
		String createCode = null;
		ActiveCode code =null;
		int i =0;
		while (i < codeNumber){
			createCode = UUID.randomUUID().toString().replaceAll("-","")+System.currentTimeMillis();
			//查询对应的激活码是否存在
			ActiveCode activeCode = activeCodeMapper.selectActiveCodeByCode(createCode);
			if(activeCode != null){
				continue;
			}
			//生成激活码
			code = new ActiveCode();
			code.setCode(createCode);
			code.setCreateDate(new Date());
			activeCodeMapper.insertActiveCode(code);
			i++;
		}
	    return 1;
	}

	/**
	 * 给注册公司分配激活码
	 * @param ids 激活码id
	 * @param companyId 公司id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateActiveCode(String ids, Integer companyId) throws Exception {
		if(companyId == null || companyId <= 0){
			throw  new Exception("请选择公司");
		}
		if(StringUtils.isEmpty(ids)){
			throw new Exception("请选择需要分配的的激活码");
		}
		//查询对应公司是否存在
		DevCompany company = devCompanyMapper.selectDevCompanyById(companyId);
		if(company == null){
			throw new Exception("分配公司不存在");
		}
		//分配激活码
		for (String idstr : Convert.toStrArray(ids)) {
			ActiveCode code = activeCodeMapper.selectActiveCodeById(Integer.parseInt(idstr));
			if(code != null && code.getDisSign() != 1 && code.getActSign() != 1){
				code.setCompanyId(companyId);
				code.setDisSign(1);
				code.setDisDate(new Date());
				activeCodeMapper.updateActiveCode(code);
			}
		}
		return 1;
	}


	/**
	 * 激活码激活
	 * @param apiActiveCode
	 * @return
	 */
	@Override
	public Map<String, Object> activeCode(ApiActiveCode apiActiveCode) {
		Map<String,Object> map = new HashMap<>(16);
		if(apiActiveCode == null || StringUtils.isEmpty(apiActiveCode.getCode())){
			map.put("code",0);
			map.put("msg","激活异常");
			return map;
		}
		// ActiveCode imeiCode = activeCodeMapper.selectActiveCodeByImei(apiActiveCode.getImei());
		// if(imeiCode != null){
		// 	map.put("code",1);
		// 	map.put("msg","设备已经激活");
		// 	return map;
		// }
		//查询对应的激活码
		ActiveCode activeCode = activeCodeMapper.selectActiveCodeByCode(apiActiveCode.getCode());
		if(activeCode == null){
			map.put("code",0);
			map.put("msg","激活失败,激活码不存在");
			return map;
		}
		if(activeCode.getActSign() != null && activeCode.getActSign() == 1){
			map.put("code",0);
			map.put("msg","激活失败，激活码失效");
			return map;
		}
		// activeCode.setImei(apiActiveCode.getImei());
		activeCode.setActSign(1);
		activeCode.setActDate(new Date());
		activeCode.setActiveIp(ServletUtils.getRequest().getRemoteAddr());
		activeCodeMapper.updateActiveCode(activeCode);
		map.put("code",1);
		map.put("msg","激活成功");
		return map;
	}

	/**
	 * 硬件和激活码绑定操作
	 * @param activeCode 绑定信息
	 * @return 结果
	 */
	@Override
	public Map<String, Object> relationDevCode(ApiActiveCode activeCode) {
		Map<String,Object> map = new HashMap<>(16);
		if (activeCode == null || StringUtils.isEmpty(activeCode.getCode()) || StringUtils.isEmpty(activeCode.getWatchCode())) {
			map.put("code",0);
			map.put("msg","绑定异常");
			return map;
		}
		ActiveCode uniqueActive = activeCodeMapper.selectActiveCodeByCode(activeCode.getCode());
		if (uniqueActive == null) {
			map.put("code",0);
			map.put("msg","激活码不存在或者失效");
			return map;
		}
		// 更新硬件绑定信息
		uniqueActive.setImei(activeCode.getWatchCode());
		activeCodeMapper.updateActiveCode(uniqueActive);
		map.put("code",1);
		map.put("msg","绑定成功");
		return map;
	}

	/**
	 * 通过看板编码查询关联的激活码信息
	 * @param watchCode 看板硬件编码
	 * @return 结果
	 */
	@Override
	public ActiveCode selectActiveCodeByImei(String watchCode) {
		return activeCodeMapper.selectActiveCodeByImei(watchCode);
	}

	/**
	 * 更换激活码
	 * @param activeCode 绑定信息
	 * @return 结果
	 */
	@Override
	public Map<String, Object> changeActiveCode(ApiActiveCode activeCode) {
		Map<String,Object> map = new HashMap<>(16);
		if (activeCode == null || StringUtils.isEmpty(activeCode.getWatchCode())) {
			map.put("code",0);
			map.put("msg","更新失败");
			return map;
		}
		// 生产新的激活码
		String createCode = UUID.randomUUID().toString().replaceAll("-","")+System.currentTimeMillis();
		//查询对应的激活码是否存在
		ActiveCode uniqueCode = activeCodeMapper.selectActiveCodeByCode(createCode);
		if(uniqueCode != null){
			map.put("code",0);
			map.put("msg","更新失败,请重新点击更新");
			return map;
		}
		ActiveCode activeCodeByImei = activeCodeMapper.selectActiveCodeByImei(activeCode.getWatchCode());
		if (activeCodeByImei == null) {
			map.put("code",0);
			map.put("msg","无关联信息");
			return map;
		}
		// 通过硬件编码重设激活码
		activeCodeByImei.setActSign(0);
		activeCodeByImei.setCode(createCode);
		activeCodeMapper.updateActiveCode(activeCodeByImei);
		map.put("code",1);
		map.put("data",createCode);
		map.put("msg","更新成功");
		return map;
	}
}
