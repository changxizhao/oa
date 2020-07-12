package com.change.workflow.action.assets;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.change.workflow.action.entities.AssetsVO;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * �ʲ�̨��ת��ģ����Ȩ���ع�
 * @author changxizhao
 */
public class AssetsDataRightRebuildAction extends BaseBean implements Action {

	private Logger log = LoggerFactory.getLogger(AssetsDataRightRebuildAction.class);
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String execute(RequestInfo requestinfo) {
		String beginTime = format.format(new Date());
		log.info("-------------------�ʲ�̨������Ȩ���ع���ʼ" + beginTime + "-------------------");
		String requestid = requestinfo.getRequestid();//����ID
		String tablename = requestinfo.getRequestManager().getBillTableName();//������
		
		RecordSet main_rs = new RecordSet();
		String main_sql = "select id from " + tablename + " where requestid = " + requestid;
		main_rs.execute(main_sql);
		main_rs.next();
		String id = Util.null2String(main_rs.getString("id"));
		
		String detailTableName = tablename + "_dt1";
		String zcbh_sql = "select zcbh from " + detailTableName + " where mainid = " + id;
		log.info("��ѯ��ϸ���ʲ���� sql = " + zcbh_sql);
		RecordSet rs = new RecordSet();
		rs.execute(zcbh_sql);
		String zcbhs = "";
		while(rs.next()){
			String zcbh = Util.null2String(rs.getString("zcbh"));
			if(!"".equals(zcbh)) {
				zcbhs += "'" + zcbh + "',";
			}
		}
		if(zcbhs.length() > 0) {
			zcbhs = zcbhs.substring(0, zcbhs.length() - 1); // ���ʲ��������ɶ��Ÿ������ַ���
			log.info("�ʲ���� = " + zcbhs);
			String zctz_sql = "select id,formmodeid,modedatacreater from uf_zctz where zcbm in (" + zcbhs + ")";
			log.info("��ѯ��ģ��sql  = " + zctz_sql);
			RecordSet zctz_rs = new RecordSet();
			zctz_rs.execute(zctz_sql);
			List<AssetsVO> voList = new ArrayList<AssetsVO>();
			while(zctz_rs.next()) {
				AssetsVO vo = new AssetsVO();
				Integer billid = Util.getIntValue(zctz_rs.getString("id")); // ��ȡ��ģ������id
				Integer modedatacreater = Util.getIntValue(zctz_rs.getString("modedatacreater")); // ��ȡ��ģ������id
				Integer formmodeid = Util.getIntValue(zctz_rs.getString("formmodeid")); // ��ȡ��ģ������id
				vo.setBillid(billid);
				vo.setCreater(modedatacreater);
				vo.setModeId(formmodeid);
				log.info("��ѯ����Ҫ�ع�Ȩ�޵����� = " + vo.toString());
				voList.add(vo);
			}
			log.info("��ѯ����Ҫ�ع�Ȩ�޵����� = " + voList);
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.setNewRight(true);
			for (AssetsVO vo : voList) {
				ModeRightInfo.rebuildModeDataShareByEdit(vo.getCreater(),vo.getModeId(),vo.getBillid());
			}
			
		}else {
			log.info("�ʲ���Ż�ȡΪ��");
		}
		String endTime = format.format(new Date());
		log.info("-------------------�ʲ�̨������Ȩ���ع�����" + endTime + "-------------------");
		return null;
	}

}
