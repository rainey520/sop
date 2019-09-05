package com.ruoyi.project.code.activeCode.service;

import java.util.*;

import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.code.activeCode.domain.ApiActiveCode;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.code.activeCode.mapper.ActiveCodeMapper;
import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import com.ruoyi.project.code.activeCode.service.IActiveCodeService;
import com.ruoyi.common.support.Convert;

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
		Map<String,Object> map = new HashMap<>();
		if(apiActiveCode == null || StringUtils.isEmpty(apiActiveCode.getCode()) || StringUtils.isEmpty(apiActiveCode.getImei())){
			map.put("code",0);
			map.put("msg","激活异常");
			return map;
		}
		ActiveCode imeiCode = activeCodeMapper.selectActiveCodeByImei(apiActiveCode.getImei());
		if(imeiCode != null){
			map.put("code",1);
			map.put("msg","设备已经激活");
			return map;
		}
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
		activeCode.setImei(apiActiveCode.getImei());
		activeCode.setActSign(1);
		activeCode.setActDate(new Date());
		activeCode.setActiveIp(ServletUtils.getRequest().getRemoteAddr());
		activeCodeMapper.updateActiveCode(activeCode);
		map.put("code",1);
		map.put("msg","激活成功");
		return map;
	}
}
