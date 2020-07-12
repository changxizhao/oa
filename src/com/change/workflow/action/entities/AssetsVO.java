package com.change.workflow.action.entities;

public class AssetsVO {
	
	private Integer billid;
	
	private Integer creater;
	
	private Integer modeId;

	public Integer getBillid() {
		return billid;
	}

	public void setBillid(Integer billid) {
		this.billid = billid;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Integer getModeId() {
		return modeId;
	}

	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}

	@Override
	public String toString() {
		return "AssetsVO [billid=" + billid + ", creater=" + creater
				+ ", modeId=" + modeId + "]";
	} 
	
	

}
