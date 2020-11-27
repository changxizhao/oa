package com.change.workflow.action.assets;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

public class ClearAssetsWorkflowDataRights extends BaseAction {

	private Logger log = LoggerFactory.getLogger(ClearAssetsWorkflowDataRights.class);

	public String execute(RequestInfo requestinfo) {

		// 清除流程权限
		String getFormmodeidSql = "select distinct formmodeid from uf_zctz";
		RecordSet getFormmodeidRs = new RecordSet();
		getFormmodeidRs.execute(getFormmodeidSql);
		if (getFormmodeidRs.next()) {
			String formmodeid = Util.null2String(getFormmodeidRs.getString("formmodeid"));
			clearWorkflowDataRights(formmodeid);
		}

		return "1";
	}

	public void clearWorkflowDataRights(String formmodeid) {
		log.info("-------------------清除流程权限开始-------------------");
		RecordSet rs1 = new RecordSet();
		String deleteWorkflowDataRightSql = "delete from modedatashare_"
				+ formmodeid
				+ " where  isdefault = 0  and (requestid is not null or requestid <> '')";
		log.info("清除流程权限modedatashare_" + formmodeid + " sql = "
				+ deleteWorkflowDataRightSql);
		boolean r1 = rs1.execute(deleteWorkflowDataRightSql);
		log.info("清除流程权限modedatashare_" + formmodeid + "结果  = " + r1);

		RecordSet rs2 = new RecordSet();
		String deleteWorkflowDataRightSetSql = "delete from modedatashare_"
				+ formmodeid
				+ "_set where  isdefault = 0  and (requestid is not null or requestid <> '')";
		log.info("清除流程权限modedatashare_" + formmodeid + "_set sql = "
				+ deleteWorkflowDataRightSetSql);
		boolean r2 = rs2.execute(deleteWorkflowDataRightSetSql);
		log.info("清除流程权限modedatashare_" + formmodeid + "_set结果  = " + r2);

		log.info("-------------------清除流程权限结束-------------------");
	}

}
