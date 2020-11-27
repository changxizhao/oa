package com.change.workflow.action.inventory.util;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.DetailTable;
import weaver.soa.workflow.request.DetailTableInfo;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.RequestService;
import weaver.soa.workflow.request.Row;
import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowDetailTableInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowServiceImpl;
import weaver.workflow.workflow.WorkTypeComInfo;
import weaver.workflow.workflow.WorkflowComInfo;

public class WorkflowServiceUtil extends WorkflowServiceImpl {
	
	private RequestService requestService = new RequestService();

	@Override
	public String doCreateWorkflowRequest(WorkflowRequestInfo wri, int userid) {
		try {
			wri = getActiveWorkflowRequestInfo(wri);
			RequestInfo ri = this.toRequestInfo(wri);
			if (ri.getCreatorid() == null || ri.getCreatorid().isEmpty()) {
				ri.setCreatorid(String.valueOf(userid));
			}

			if (!ri.getCreatorid().equals(String.valueOf(userid))) {
				ri.setCreatorid(String.valueOf(userid));
			}

			//ri.setIsNextFlow(wri.getIsnextflow());
			return this.requestService.createRequest(ri);
		} catch (Exception var4) {
			var4.printStackTrace();
			this.writeLog(var4);
			return null;
		}
	}
	
	private WorkflowRequestInfo getActiveWorkflowRequestInfo(
			WorkflowRequestInfo wri) {
		WorkflowComInfo wfComInfo = new WorkflowComInfo();
		WorkTypeComInfo WorkflowTypeComInfo = new WorkTypeComInfo();
		WorkflowBaseInfo oldWorkflowBaseInfo = wri.getWorkflowBaseInfo();
		String activewfid = this.getActiveversionid(oldWorkflowBaseInfo
				.getWorkflowId());
		oldWorkflowBaseInfo.setWorkflowName(wfComInfo
				.getWorkflowname(activewfid));
		oldWorkflowBaseInfo.setWorkflowTypeId(wfComInfo
				.getWorkflowtype(activewfid));
		oldWorkflowBaseInfo.setWorkflowTypeName(WorkflowTypeComInfo
				.getWorkTypename(wfComInfo.getWorkflowtype(activewfid)));
		oldWorkflowBaseInfo.setWorkflowId(activewfid);
		wri.setWorkflowBaseInfo(oldWorkflowBaseInfo);
		return wri;
	}
	
	private String getActiveversionid(String workflowid) {
		String wfid = workflowid;
		RecordSet rs = new RecordSet();
		String sql = "select activeversionid from workflow_base  where id='"
				+ workflowid + "'";
		rs.executeSql(sql);
		String tempwfid = "";
		if (rs.next()) {
			tempwfid = Util.null2String(rs.getString("activeversionid"));
		}

		if (!tempwfid.equals("")) {
			wfid = tempwfid;
		}

		return wfid;
	}
	
	private RequestInfo toRequestInfo(WorkflowRequestInfo wri) throws Exception {
		if (wri != null && wri.getWorkflowBaseInfo() != null) {
			int formid = 0;
			String isbill = "0";
			RecordSet rs = new RecordSet();
			int workflowid = Util.getIntValue(wri.getWorkflowBaseInfo()
					.getWorkflowId(), 0);
			rs.executeProc("workflow_Workflowbase_SByID",
					String.valueOf(workflowid));
			if (rs.next()) {
				formid = Util.getIntValue(rs.getString("formid"), 0);
				isbill = "" + Util.getIntValue(rs.getString("isbill"), 0);
			}

			if ("1".equals(isbill) && formid == 158) {
				String amount = "0";
				WorkflowDetailTableInfo[] wdtis = wri
						.getWorkflowDetailTableInfos();
				WorkflowRequestTableRecord[] wrtrs;
				int j;
				if (wdtis != null) {
					for (int i = 0; i < wdtis.length; ++i) {
						wrtrs = wdtis[i].getWorkflowRequestTableRecords();
						if (wrtrs != null) {
							for (j = 0; j < wrtrs.length; ++j) {
								if (wrtrs[j] != null
										&& wrtrs[j].getRecordOrder() == -1) {
									WorkflowRequestTableField[] wrtfs = wrtrs[j]
											.getWorkflowRequestTableFields();
									if (wrtfs != null) {
										for (int k = 0; k < wrtfs.length; ++k) {
											if (wrtfs[k] != null
													&& "amount".equals(wrtfs[k]
															.getFieldName())) {
												amount = wrtfs[k]
														.getFieldValue();
											}
										}
									}
								}
							}
						}
					}
				}

				WorkflowMainTableInfo wmti = wri.getWorkflowMainTableInfo();
				if (wmti != null) {
					wrtrs = wmti.getRequestRecords();
					if (wrtrs != null && wrtrs[0] != null) {
						for (j = 0; j < wrtrs[0]
								.getWorkflowRequestTableFields().length; ++j) {
							WorkflowRequestTableField wrtf = wrtrs[0]
									.getWorkflowRequestTableFields()[j];
							if (wrtf != null
									&& "total".equals(wrtf.getFieldName())) {
								wrtf.setFieldValue(amount);
							}
						}
					}
				}
			}

			RequestInfo requestInfo = new RequestInfo();
			if (Util.getIntValue(wri.getRequestId()) > 0) {
				requestInfo = this.requestService.getRequest(
						Util.getIntValue(wri.getRequestId()), 10);
			}

			requestInfo.setRequestid(wri.getRequestId());
			requestInfo
					.setWorkflowid(wri.getWorkflowBaseInfo().getWorkflowId());
			requestInfo.setCreatorid(wri.getCreatorId());
			requestInfo.setDescription(Util.processHtmlUnicode(wri
					.getRequestName()));
			requestInfo.setRequestlevel(wri.getRequestLevel());
			requestInfo.setRemindtype(wri.getMessageType());
			//requestInfo.set_Remark(wri.getRemark() == null ? "" : wri.getRemark());
			MainTableInfo mainTableInfo = new MainTableInfo();
			List fields = new ArrayList();
			WorkflowMainTableInfo wmti = wri.getWorkflowMainTableInfo();
			if (wmti != null) {
				WorkflowRequestTableRecord[] wrtrs = wmti.getRequestRecords();
				if (wrtrs != null && wrtrs[0] != null) {
					for (int i = 0; i < wrtrs[0]
							.getWorkflowRequestTableFields().length; ++i) {
						WorkflowRequestTableField wrtf = wrtrs[0]
								.getWorkflowRequestTableFields()[i];
						if (wrtf != null && wrtf.getFieldName() != null
								&& !"".equals(wrtf.getFieldName())
								&& wrtf.getFieldValue() != null
								&& !"".equals(wrtf.getFieldValue())
								&& wrtf.isView() && wrtf.isEdit()) {
							Property field = new Property();
							field.setName(wrtf.getFieldName());
							field.setValue(Util.processHtmlUnicode(wrtf
									.getFieldValue()));
							field.setType(wrtf.getFieldType());
							fields.add(field);
						}
					}
				}
			}

			Property[] fieldarray = (Property[]) fields
					.toArray(new Property[fields.size()]);
			mainTableInfo.setProperty(fieldarray);
			requestInfo.setMainTableInfo(mainTableInfo);
			DetailTableInfo detailTableInfo = new DetailTableInfo();
			WorkflowDetailTableInfo[] wdtis = wri.getWorkflowDetailTableInfos();
			List detailTables = new ArrayList();

			for (int i = 0; wdtis != null && i < wdtis.length; ++i) {
				DetailTable detailTable = new DetailTable();
				detailTable.setId(String.valueOf(i + 1));
				WorkflowDetailTableInfo wdti = wdtis[i];
				if (wdti != null) {
					detailTable.setTableDBName(wdti.getTableDBName() == null
							? ""
							: wdti.getTableDBName());
					WorkflowRequestTableRecord[] wrtrs = wdti
							.getWorkflowRequestTableRecords();
					List rows = new ArrayList();

					for (int j = 0; wrtrs != null && j < wrtrs.length; ++j) {
						Row row = new Row();
						WorkflowRequestTableRecord wrtr = wrtrs[j];
						if (wrtr.getRecordOrder() > 0) {
							row.setId(String.valueOf(wrtr.getRecordOrder()));
						} else {
							row.setId("");
						}

						WorkflowRequestTableField[] wrtfs = wrtr
								.getWorkflowRequestTableFields();
						List cells = new ArrayList();

						for (int k = 0; wrtfs != null && k < wrtfs.length; ++k) {
							WorkflowRequestTableField wrtf = wrtfs[k];
							if (wrtf != null && wrtf.getFieldName() != null
									&& !"".equals(wrtf.getFieldName())
									&& wrtf.getFieldValue() != null
									&& !"".equals(wrtf.getFieldValue())
									&& wrtf.isView() && wrtf.isEdit()) {
								Cell cell = new Cell();
								cell.setName(wrtf.getFieldName());
								cell.setValue(Util.processHtmlUnicode(wrtf
										.getFieldValue()));
								cell.setType(wrtf.getFieldType());
								cells.add(cell);
							}
						}

						if (cells != null && cells.size() > 0) {
							Cell[] cellarray = (Cell[]) cells
									.toArray(new Cell[cells.size()]);
							row.setCell(cellarray);
						}

						rows.add(row);
					}

					if (rows != null && rows.size() > 0) {
						Row[] rowarray = (Row[]) rows.toArray(new Row[rows
								.size()]);
						detailTable.setRow(rowarray);
					}

					detailTables.add(detailTable);
				}
			}

			DetailTable[] detailTablearray = (DetailTable[]) detailTables
					.toArray(new DetailTable[detailTables.size()]);
			detailTableInfo.setDetailTable(detailTablearray);
			requestInfo.setDetailTableInfo(detailTableInfo);
			return requestInfo;
		} else {
			return null;
		}
	}

}
