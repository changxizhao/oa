package com.change.workflow.action.inventory.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.change.cronjob.utils.DateUtils;
import com.change.workflow.action.inventory.entities.InventoryDetail;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.BaseAction;
import weaver.soa.workflow.request.RequestInfo;

public class InventoryReport  extends BaseAction {
	
	private static BaseBean bb = new BaseBean();
	
	public String execute(RequestInfo request) {
		bb.writeLog("输出报告开始...");
		Map<String, Collection<?>> wp = getInventoryByType(0); // 未盘
		Map<String, Collection<?>> yp = getInventoryByType(1); // 已盘
		
		Map<String, Collection<?>> py = getInventoryByType(2); // 盘盈
		Map<String, Collection<?>> pk = getInventoryByType(3); // 盘亏
		
		Map<String, Collection<?>> yd = getInventoryByType(4); // 异动
		
		Set<String> pyIds = (Set<String>)py.get("id");
		Set<String> pkIds = (Set<String>)pk.get("id");
		
		List<InventoryDetail> pyList = (List<InventoryDetail>)py.get("inventory"); // 盘盈的数据
		List<InventoryDetail> pkList = (List<InventoryDetail>)pk.get("inventory"); // 盘亏的数据
		
		Set<String> idSet = new HashSet<String>();
		idSet.addAll(pkIds);
		idSet.addAll(pyIds);
		
		idSet.removeAll(pyIds);
		pkIds.removeAll(idSet); // pkIds此时为盘盈和盘亏的交集
		
		// 遍历盘盈的数据，剔除既盘亏又盘盈的数据
		List<InventoryDetail> ydList = new ArrayList<InventoryDetail>();
		for (int i = 0; i < pyList.size(); i++) {
			InventoryDetail detail = pyList.get(i);
			if(pkIds.contains(detail.getId())) {
				pyList.remove(i);
				ydList.add(detail);
			}
			if("".equals(detail.getZcbm())) {
				pyList.remove(i);
			}
		}
		
		// 遍历盘亏的数据，剔除既盘亏又盘盈的数据
		for (int i = 0; i < pkList.size(); i++) {
			InventoryDetail detail = pkList.get(i);
			if (pkIds.contains(detail.getId())) {
				pkList.remove(i);
			}
			if ("".equals(detail.getZcbm())) {
				pkList.remove(i);
			}
		}
		
		ydList.addAll((List<InventoryDetail>)yd.get("inventory")); // ydList 是最终为异动的数据
		doWriteIntoReport((List<InventoryDetail>) wp.get("inventory"), "0");
		doWriteIntoReport((List<InventoryDetail>) yp.get("inventory"), "1");
		doWriteIntoReport(pyList, "2");
		doWriteIntoReport(pkList, "3");
		doWriteIntoReport(ydList, "4");
		return "1";
		
	}
	
	void doWriteIntoReport(List<InventoryDetail> inventoryDetail, String status) {
		RecordSet rs = new RecordSet();
		String hzpdb_formmodeid = Prop.getPropValue("AssetsInventory", "hzpdb_formmodeid");
		rs.execute("delete from uf_hzpdb");
		for (InventoryDetail detail : inventoryDetail) {
			String sql = "insert into uf_hzpdb(pdr,pdlx,zczt,szkf,sybm,syr,syrgh,"
					+ "sydd,zclx,pdrq,pdsj,zcmc,gg,zcbm,xh,sjsyr,sjsydw,sjsydd,"
					+ "pdzt,sjsyrgh,sjszkf,jhbhwb,pdrwbhwb,jhbh,"
					+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime)"
					+ " values('"+detail.getPdr()+"','"+detail.getPdlx()+"','"+detail.getZczt()+"','"+detail.getSzkf()+"','"+detail.getSybm()+"','"+detail.getSyr()+"',"
					+ "'"+detail.getSyrgh()+"','"+detail.getSydd()+"','"+detail.getZclx()+"','"+detail.getPdrq()+"','"+detail.getPdsj()+"','"+detail.getZcmc()+"','"+detail.getGg()+"','"+detail.getZcbm()+"','"+detail.getXh()+"',"
					+ "'"+detail.getSjsyr()+"','"+detail.getSjsydw()+"','"+detail.getSjsydd()+"','"+status+"','"+detail.getSjsyrgh()+"','"+detail.getSjszkf()+"','"+detail.getJhbhwb()+"','"+detail.getPdrwbhwb()+"','"+detail.getJhbh()+"',"
					+ "'"+hzpdb_formmodeid+"',1,0,'"+DateUtils.getNowStr("yyyy-MM-dd")+"','"+DateUtils.getNowStr("HH:mm:dd")+"')";
			bb.writeLog("报告sql ：" + sql);
			rs.execute(sql);
		}
	}

	private Map<String, Collection<?>> getInventoryByType(int type) {
		RecordSet rs = new RecordSet();
		String sql = "select id,requestId,pdlx,sydd,pdrq,pdsj,jhbh,pdr,zczt,szkf,sybm,syr,syrgh,zclx,zcbm,zcmc,gg,xh,sjsyr,sjsydw,sjsydd,pdzt,sjsyrgh,jhbhn,sjszkf,bz,jhbhwb,pdrwbhwb,sdbs,pdksrq,pdjsrq from uf_dwkczcmxb where pdzt = " + type;
		rs.execute(sql);
		List<InventoryDetail> list = new ArrayList<InventoryDetail>();
		Set<String> idSet = new HashSet<String>();
		while(rs.next()) {
			InventoryDetail detail = new InventoryDetail();
			String id = Util.null2String(rs.getString("id"));
			String pdlx = Util.null2String(rs.getString("pdlx"));
			String sydd = Util.null2String(rs.getString("sydd"));
			String pdrq = Util.null2String(rs.getString("pdrq"));
			String pdsj = Util.null2String(rs.getString("pdsj"));
			String jhbh = Util.null2String(rs.getString("jhbh"));
			String pdr = Util.null2String(rs.getString("pdr"));
			String zczt = Util.null2String(rs.getString("zczt"));
			String szkf = Util.null2String(rs.getString("szkf"));
			String sybm = Util.null2String(rs.getString("sybm"));
			String syr = Util.null2String(rs.getString("syr"));
			String syrgh = Util.null2String(rs.getString("syrgh"));
			String zclx = Util.null2String(rs.getString("zclx"));
			String zcbm = Util.null2String(rs.getString("zcbm"));
			String zcmc = Util.null2String(rs.getString("zcmc"));
			String gg = Util.null2String(rs.getString("gg"));
			String xh = Util.null2String(rs.getString("xh"));
			String sjsyr = Util.null2String(rs.getString("sjsyr"));//
			if(isEmpty(sjsyr)) {
				sjsyr = syr;
			}
			String sjsydw = Util.null2String(rs.getString("sjsydw"));//
			if(isEmpty(sjsydw)) {
				sjsydw = sybm;
			}
			String sjsydd = Util.null2String(rs.getString("sjsydd"));//
			if(isEmpty(sjsydd)) {
				sjsydd = sydd;
			}
			String pdzt = Util.null2String(rs.getString("pdzt"));
			String sjsyrgh = Util.null2String(rs.getString("sjsyrgh"));//
			if(isEmpty(sjsyrgh)) {
				sjsyrgh = syrgh;
			}
			String jhbhn = Util.null2String(rs.getString("jhbhn"));
			String sjszkf = Util.null2String(rs.getString("sjszkf"));//
			if(isEmpty(sjszkf)) {
				sjszkf = szkf;
			}
			String bz = Util.null2String(rs.getString("bz"));
			String jhbhwb = Util.null2String(rs.getString("jhbhwb"));
			String pdrwbhwb = Util.null2String(rs.getString("pdrwbhwb"));
			String sdbs = Util.null2String(rs.getString("sdbs"));
			String pdksrq = Util.null2String(rs.getString("pdksrq"));
			String pdjsrq = Util.null2String(rs.getString("pdjsrq"));
			detail.setZczt(zczt);
			detail.setZcmc(zcmc);
			detail.setZclx(zclx);
			detail.setZcbm(zcbm);
			detail.setXh(xh);
			detail.setSzkf(sjszkf);
			detail.setSyrgh(sjsyrgh);
			detail.setSyr(syr);
			detail.setSydd(sydd);
			detail.setSybm(sybm);
			detail.setSjszkf(sjszkf);
			detail.setSjsyrgh(sjsyrgh);
			detail.setSjsyr(sjsyr);
			detail.setBz(bz);
			detail.setGg(gg);
			detail.setId(id);
			detail.setJhbh(jhbh);
			detail.setJhbhn(jhbhn);
			detail.setJhbhwb(jhbhwb);
			detail.setPdjsrq(pdjsrq);
			detail.setPdksrq(pdksrq);
			detail.setPdlx(pdlx);
			detail.setPdr(pdr);
			detail.setPdrq(pdrq);
			detail.setPdrwbhwb(pdrwbhwb);
			detail.setPdsj(pdsj);
			detail.setPdzt(pdzt);
			detail.setSdbs(sdbs);
			detail.setSjsydd(sjsydd);
			detail.setSzkf(szkf);
			detail.setSyrgh(syrgh);
			detail.setSjsydw(sjsydw);
			list.add(detail);
			idSet.add(id);
		}
		Map<String, Collection<?>> map = new HashMap<String, Collection<?>>();
		map.put("id", idSet);
		map.put("inventory", list);
		return map;
	}
	
	public static boolean isEmpty(String str) {
		if(str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Set<String> s = new HashSet<String>();
		Set<String> l1 = new HashSet<String>();
		l1.add("1");
		l1.add("2");
		l1.add("3");
		l1.add("4");
		l1.add("5");
		l1.add("6");
		Set<String> l2 = new HashSet<String>();
		l2.add("4");
		l2.add("5");
		l2.add("6");
		l2.add("7");
		l2.add("8");
		
		s.addAll(l1);
		s.addAll(l2);
		
		s.removeAll(l1);
		
		l2.removeAll(s);
		
		List<String> l = new ArrayList<String>();
		l.add("1");
		l.add("2");
		l.add("3");
		l.add("4");
		for (int i = 0; i < l.size(); i++) {
			String string = l.get(i);
			if("2".equals(string)) {
				l.remove(i);
			}
		}
		
		System.out.println(l);
	}

}
