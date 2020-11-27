package com.change.workflow.action.inventory.util;

import java.util.List;

import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowDetailTableInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowService;
import weaver.workflow.webservices.WorkflowServiceImpl;


/**
 * 
 * @author changxizhao
 */
public class FlowUtil extends BaseBean{
	
	private Logger logger = LoggerFactory.getLogger(FlowUtil.class);
	
	/*
	 * 创建流程 只支持 一个明细表数据
	 * @param workflowId 工作流Id
	 * @param createId 创建人Id
	 * @param workflowTitle 流程标题
	 * @param jjcd 紧急程度
	 * @param paramKey 主表字段
	 * @param paramVal 主表字段值
	 * @param detailKey 明细表字段
	 * @param detailVal 明细表字段值
	 * @return String 返回创建的流程RequestId
	 * @return isNextFlow 是否流转到下一节点
	 * @author lsc
	 * @date 2018-7-30 下午5:04:49
	 */
	public String createFlow(String workflowId, String createId, String workflowTitle,String jjcd
			, String[] paramKey, String[] paramVal, String[] detailKey, List<String[]> detailVal,String isNextFlow) {

		logger.info("----------createFlow start----------");
		
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[paramKey.length];

		//主表
		for (int i = 0; i < paramKey.length; i++) {
			
			logger.info("----------paramKey["+i+"]----------"+paramKey[i]);
			logger.info("----------paramVal["+i+"]----------"+paramVal[i]);

			wrti[i] = new WorkflowRequestTableField();
			wrti[i].setFieldName(paramKey[i]);// 对应字段
			wrti[i].setFieldValue(paramVal[i]); // 对应值
			wrti[i].setView(true);// 字段是否可见
			wrti[i].setEdit(true);// 字段是否可编辑
		}
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];// 主字段只有一行数据
		wrtri[0] = new WorkflowRequestTableRecord();
		wrtri[0].setWorkflowRequestTableFields(wrti);
		
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
		wmi.setRequestRecords(wrtri);
		
		logger.info("----------main ok----------");
		
		WorkflowDetailTableInfo[] workflowDetailTableInfo = null;
		
		//验证子表字段是否存在
		if(detailKey!=null && detailVal!=null && !"".equals(detailKey) && !"".equals(detailVal)){
			
			wrtri = new WorkflowRequestTableRecord[detailVal.size()];//添加指定条数行明细数据
			
			logger.info("----------details start----------");
			
			for(int j=0; j<detailVal.size(); j++){ 
				
				String[] val=detailVal.get(j);//字段值
				
				wrti = new WorkflowRequestTableField[val.length];  //每行的字段信息
				
				logger.info("----------details val.length"+val.length);
				for(int i=0; i<val.length; i++){
					
					logger.info("----------detailKey[i] : "+detailKey[i]+", val[i] :"+val[i]);
					
					wrti[i] = new WorkflowRequestTableField();             
					wrti[i].setFieldName(detailKey[i]);// 对应字段
					wrti[i].setFieldValue(val[i]);// 对应值
					wrti[i].setView(true);//字段是否可见
					wrti[i].setEdit(true);//字段是否可编辑
				}
				
				wrtri[j] = new WorkflowRequestTableRecord();
				wrtri[j].setWorkflowRequestTableFields(wrti);
			}
			
			//指定明细表的个数，多个明细表指定多个，顺序按照明细的顺序	
			workflowDetailTableInfo = new WorkflowDetailTableInfo[1];
			workflowDetailTableInfo[0] = new WorkflowDetailTableInfo();
			workflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
			
			logger.info("----------details ok----------");
		}
		
		//流程 基本信息
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();
		wbi.setWorkflowId(workflowId);
		WorkflowRequestInfo wri = new WorkflowRequestInfo();// 流程基本信息
		wri.setCreatorId(createId);// 创建人id
		wri.setRequestLevel(jjcd);// 0 正常，1重要，2紧急
		wri.setRequestName(workflowTitle);// 流程标题
		wri.setWorkflowMainTableInfo(wmi);// 添加主字段数据
		if(workflowDetailTableInfo != null)
			wri.setWorkflowDetailTableInfos(workflowDetailTableInfo);//添加明细数据
		wri.setWorkflowBaseInfo(wbi);
		//wri.setIsnextflow(isNextFlow);
		//WorkflowService workflowService = new WorkflowServiceImpl();
		//String requestid = workflowService.doCreateWorkflowRequest(wri,	Util.getIntValue(createId));// 返回requestid
		WorkflowServiceUtil workflowService = new WorkflowServiceUtil();
		String requestid = workflowService.doCreateWorkflowRequest(wri,	Util.getIntValue(createId));// 返回requestid
		logger.info("流程requestid:" + requestid + "，触发成功！");
		return requestid;
	}

}
