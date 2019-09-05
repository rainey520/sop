package com.ruoyi.project.code.activeCode.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 激活码表 tab_active_code
 * 
 * @author sj
 * @date 2019-08-26
 */
public class ActiveCode extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 激活码 */
	private String code;
	/** 手机IMEI号 */
	private String imei;
	/** 是否激活0、否 1、是 */
	private Integer actSign;
	/** 分配公司 */
	private Integer companyId;
	private String comName;
	/** 是否分配 */
	private Integer disSign;
	/** 激活IP */
	private String activeIp;
	/** 创建时间 */
	private Date createDate;
	/** 分配时间 */
	private Date disDate;
	/** 激活时间 */
	private Date actDate;

	private Date actDateEnd;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	public void setImei(String imei) 
	{
		this.imei = imei;
	}

	public String getImei() 
	{
		return imei;
	}
	public void setActSign(Integer actSign) 
	{
		this.actSign = actSign;
	}

	public Integer getActSign() 
	{
		return actSign;
	}
	public void setCompanyId(Integer companyId) 
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId() 
	{
		return companyId;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public void setDisSign(Integer disSign)
	{
		this.disSign = disSign;
	}

	public Integer getDisSign() 
	{
		return disSign;
	}
	public void setActiveIp(String activeIp) 
	{
		this.activeIp = activeIp;
	}

	public String getActiveIp() 
	{
		return activeIp;
	}
	public void setCreateDate(Date createDate) 
	{
		this.createDate = createDate;
	}

	public Date getCreateDate() 
	{
		return createDate;
	}
	public void setDisDate(Date disDate) 
	{
		this.disDate = disDate;
	}

	public Date getDisDate() 
	{
		return disDate;
	}
	public void setActDate(Date actDate) 
	{
		this.actDate = actDate;
	}

	public Date getActDate() 
	{
		return actDate;
	}

	public Date getActDateEnd() {
		return actDateEnd;
	}

	public void setActDateEnd(Date actDateEnd) {
		this.actDateEnd = actDateEnd;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("imei", getImei())
            .append("actSign", getActSign())
            .append("companyId", getCompanyId())
            .append("disSign", getDisSign())
            .append("activeIp", getActiveIp())
            .append("createDate", getCreateDate())
            .append("disDate", getDisDate())
            .append("actDate", getActDate())
            .toString();
    }
}
