<%@page import="weaver.general.BaseBean"%>
<%@page import="weaver.general.Util"%>
<%@page import="weaver.conn.RecordSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/systeminfo/init_wev8.jsp"%>
<% 
	BaseBean bb = new BaseBean();
		//清除流程权限
		String getFormmodeidSql = "select distinct formmodeid from uf_zctz";
		RecordSet getFormmodeidRs = new RecordSet();
		getFormmodeidRs.execute(getFormmodeidSql);
		if (getFormmodeidRs.next()) {
			String formmodeid = Util.null2String(getFormmodeidRs.getString("formmodeid"));
			bb.writeLog("-------------------清除流程权限开始jsp-------------------");
			RecordSet rs1 = new RecordSet();
			String deleteWorkflowDataRightSql = "delete from modedatashare_"
					+ formmodeid
					+ " where  isdefault = 0  and (requestid is not null or requestid <> '')";
			bb.writeLog("清除流程权限modedatashare_" + formmodeid + " sql = "
					+ deleteWorkflowDataRightSql);
			boolean r1 = rs1.execute(deleteWorkflowDataRightSql);
			bb.writeLog("清除流程权限modedatashare_" + formmodeid + "结果  = " + r1);

			RecordSet rs2 = new RecordSet();
			String deleteWorkflowDataRightSetSql = "delete from modedatashare_"
					+ formmodeid
					+ "_set where  isdefault = 0  and (requestid is not null or requestid <> '')";
			bb.writeLog("清除流程权限modedatashare_" + formmodeid + "_set sql = " + deleteWorkflowDataRightSetSql);
			boolean r2 = rs2.execute(deleteWorkflowDataRightSetSql);
			bb.writeLog("清除流程权限modedatashare_" + formmodeid + "_set结果  = " + r2);

			bb.writeLog("-------------------清除流程权限结束jsp-------------------");
		}
%>
