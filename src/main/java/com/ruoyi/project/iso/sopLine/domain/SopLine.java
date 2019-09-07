package com.ruoyi.project.iso.sopLine.domain;

import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 作业指导书  产线 配置表 tab_sop_line
 * 
 * @author sj
 * @date 2019-06-15
 */
public class SopLine extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 作业指定数id，文件夹 */
	private Integer sopId;
	/** 产线id */
	private Integer lineId;
	/**
	 * 单工位车间id
	 */
	private Integer parentId;
	/**
	 * 设备编码
	 */
	private String imName;
	/** 产品id */
	private Integer pnId;
	/** 产品编码 */
	private String pnCode;
	/** 修改者id */
	private Integer cId;
	/** 修改时间 */
	private Date cTime;
	/**
	 * sop配置标记(默认0、流水线，1、单工位)
	 */
	private Integer sopTag;
	private String lineName;//产线名称
	private String sopName;//SOP名称
	private Integer wId;//工位id
	private String workName;//工位名称
	private Integer pageId;//页Id
	private String pageName;//页名称
	private Integer sid;//主表id

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getSopId() {
		return sopId;
	}

	public void setSopId(Integer sopId) {
		this.sopId = sopId;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getImName() {
		return imName;
	}

	public void setImName(String imName) {
		this.imName = imName;
	}

	public Integer getPnId() {
		return pnId;
	}

	public void setPnId(Integer pnId) {
		this.pnId = pnId;
	}

	public String getPnCode() {
		return pnCode;
	}

	public void setPnCode(String pnCode) {
		this.pnCode = pnCode;
	}

	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Integer getSopTag() {
		return sopTag;
	}

	public void setSopTag(Integer sopTag) {
		this.sopTag = sopTag;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getSopName() {
		return sopName;
	}

	public void setSopName(String sopName) {
		this.sopName = sopName;
	}

	public Integer getwId() {
		return wId;
	}

	public void setwId(Integer wId) {
		this.wId = wId;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	@Override
	public String toString() {
		return "SopLine{" +
				"id=" + id +
				", companyId=" + companyId +
				", sopId=" + sopId +
				", lineId=" + lineId +
				", parentId=" + parentId +
				", imName='" + imName + '\'' +
				", pnId=" + pnId +
				", pnCode='" + pnCode + '\'' +
				", cId=" + cId +
				", cTime=" + cTime +
				", sopTag=" + sopTag +
				", lineName='" + lineName + '\'' +
				", sopName='" + sopName + '\'' +
				", wId=" + wId +
				", pageId=" + pageId +
				", sid=" + sid +
				'}';
	}
}
