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
	 * �������� ֻ֧�� һ����ϸ������
	 * @param workflowId ������Id
	 * @param createId ������Id
	 * @param workflowTitle ���̱���
	 * @param jjcd �����̶�
	 * @param paramKey �����ֶ�
	 * @param paramVal �����ֶ�ֵ
	 * @param detailKey ��ϸ���ֶ�
	 * @param detailVal ��ϸ���ֶ�ֵ
	 * @return String ���ش���������RequestId
	 * @return isNextFlow �Ƿ���ת����һ�ڵ�
	 * @author lsc
	 * @date 2018-7-30 ����5:04:49
	 */
	public String createFlow(String workflowId, String createId, String workflowTitle,String jjcd
			, String[] paramKey, String[] paramVal, String[] detailKey, List<String[]> detailVal,String isNextFlow) {

		logger.info("----------createFlow start----------");
		
		WorkflowRequestTableField[] wrti = new WorkflowRequestTableField[paramKey.length];

		//����
		for (int i = 0; i < paramKey.length; i++) {
			
			logger.info("----------paramKey["+i+"]----------"+paramKey[i]);
			logger.info("----------paramVal["+i+"]----------"+paramVal[i]);

			wrti[i] = new WorkflowRequestTableField();
			wrti[i].setFieldName(paramKey[i]);// ��Ӧ�ֶ�
			wrti[i].setFieldValue(paramVal[i]); // ��Ӧֵ
			wrti[i].setView(true);// �ֶ��Ƿ�ɼ�
			wrti[i].setEdit(true);// �ֶ��Ƿ�ɱ༭
		}
		
		WorkflowRequestTableRecord[] wrtri = new WorkflowRequestTableRecord[1];// ���ֶ�ֻ��һ������
		wrtri[0] = new WorkflowRequestTableRecord();
		wrtri[0].setWorkflowRequestTableFields(wrti);
		
		WorkflowMainTableInfo wmi = new WorkflowMainTableInfo();
		wmi.setRequestRecords(wrtri);
		
		logger.info("----------main ok----------");
		
		WorkflowDetailTableInfo[] workflowDetailTableInfo = null;
		
		//��֤�ӱ��ֶ��Ƿ����
		if(detailKey!=null && detailVal!=null && !"".equals(detailKey) && !"".equals(detailVal)){
			
			wrtri = new WorkflowRequestTableRecord[detailVal.size()];//���ָ����������ϸ����
			
			logger.info("----------details start----------");
			
			for(int j=0; j<detailVal.size(); j++){ 
				
				String[] val=detailVal.get(j);//�ֶ�ֵ
				
				wrti = new WorkflowRequestTableField[val.length];  //ÿ�е��ֶ���Ϣ
				
				logger.info("----------details val.length"+val.length);
				for(int i=0; i<val.length; i++){
					
					logger.info("----------detailKey[i] : "+detailKey[i]+", val[i] :"+val[i]);
					
					wrti[i] = new WorkflowRequestTableField();             
					wrti[i].setFieldName(detailKey[i]);// ��Ӧ�ֶ�
					wrti[i].setFieldValue(val[i]);// ��Ӧֵ
					wrti[i].setView(true);//�ֶ��Ƿ�ɼ�
					wrti[i].setEdit(true);//�ֶ��Ƿ�ɱ༭
				}
				
				wrtri[j] = new WorkflowRequestTableRecord();
				wrtri[j].setWorkflowRequestTableFields(wrti);
			}
			
			//ָ����ϸ��ĸ����������ϸ��ָ�������˳������ϸ��˳��	
			workflowDetailTableInfo = new WorkflowDetailTableInfo[1];
			workflowDetailTableInfo[0] = new WorkflowDetailTableInfo();
			workflowDetailTableInfo[0].setWorkflowRequestTableRecords(wrtri);
			
			logger.info("----------details ok----------");
		}
		
		//���� ������Ϣ
		WorkflowBaseInfo wbi = new WorkflowBaseInfo();
		wbi.setWorkflowId(workflowId);
		WorkflowRequestInfo wri = new WorkflowRequestInfo();// ���̻�����Ϣ
		wri.setCreatorId(createId);// ������id
		wri.setRequestLevel(jjcd);// 0 ������1��Ҫ��2����
		wri.setRequestName(workflowTitle);// ���̱���
		wri.setWorkflowMainTableInfo(wmi);// ������ֶ�����
		if(workflowDetailTableInfo != null)
			wri.setWorkflowDetailTableInfos(workflowDetailTableInfo);//�����ϸ����
		wri.setWorkflowBaseInfo(wbi);
		//wri.setIsnextflow(isNextFlow);
		//WorkflowService workflowService = new WorkflowServiceImpl();
		//String requestid = workflowService.doCreateWorkflowRequest(wri,	Util.getIntValue(createId));// ����requestid
		WorkflowServiceUtil workflowService = new WorkflowServiceUtil();
		String requestid = workflowService.doCreateWorkflowRequest(wri,	Util.getIntValue(createId));// ����requestid
		logger.info("����requestid:" + requestid + "�������ɹ���");
		return requestid;
	}

}
