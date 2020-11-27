package com.change.cronjob.cinterface;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.change.cronjob.entities.UfZctz;
import com.change.cronjob.utils.AssetUtil;
import com.change.cronjob.utils.DateUtils;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

/**  
 * @Author: changxizhao
 * @Date: 2020��6��20�� ����4:17:30
 */
public class DepreciationOfAssets extends BaseCronJob {
	
	private Logger log = LoggerFactory.getLogger(DepreciationOfAssets.class);

	public void execute() {
		log.info("�¶��ۿ۶�ʱ����ִ�п�ʼ...");
		String sql = "select id,requestId,glzz,zczt,szkf,sybm,syr,syrgh,zclx,zclxbm,"
				+ "zcmc,zcbm,gg,xh,cfdd,ly,kssyrq,zcczrq,gys,cgdw,zjqs,czl,yzjl,cgjehs,"
				+ "zcyzbhs,yzjje,yzjqsy,zcljzj,zcjz,yzcbm,lyrq,bz,glzzbm,zjsfwc,sydwzcgly,"
				//+ "sydwqtry,glzzzggly,glzzjggly,glzzqtry,kfgly,kfqtry from uf_zctz where zczt != 2 and (zjsfwc != 0 or zjsfwc is null)";
				+ "sydwqtry,glzzzggly,glzzjggly,glzzqtry,kfgly,kfqtry from uf_zctz where zczt != 2";
		String ydzj_modelid = Prop.getPropValue("AssetsModelId", "ydzj_modelid");
		List<UfZctz> assetList = AssetUtil.getAssetList(sql);
		RecordSet update_rs = new RecordSet();
		RecordSet insert_rs = new RecordSet();
		RecordSet finish_rs = new RecordSet();
		for (UfZctz ufZctz : assetList) {
			if(ufZctz.getZczt() == 2 || ufZctz.getZjsfwc() == 0) {// �ʲ�״̬������Ǳ��ϣ��򲻴��� ���۾���ɣ�ͬ��������
				continue;
			}
			log.info("->->->->->->->->->->->->->->->->->->->->->->->");
			Integer zjqs = ufZctz.getZjqs(); // �۾�����
			// 1�����۾�
			//���۾ɽ�����˰���*���۾���
			float totalPrice = (ufZctz.getZcyzbhs() * ufZctz.getYzjl()) / 100;
			BigDecimal b = new BigDecimal(totalPrice); 
			float yzjje = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); // ���۾ɽ�� 
			//���۾��������£�:��ǰ�·�-��ʼ�·ݣ������ǰ�·ݳ��������·ݲ�����
			Integer yzjqsy = ufZctz.getYzjqsy();
			try {
				yzjqsy = DateUtils.getDiffMonth(ufZctz.getKssyrq(),DateUtils.getNowStr("yyyy-MM-dd"));
				//yzjqsy = yzjqsy -1;
			} catch (ParseException e) {
				e.printStackTrace();
				log.info("�����·ݲ�ֵ����  -> " + e.toString());
			}
			//�ʲ��ۼ��۾ɣ��۾�����*���۾ɽ��
			float zcljzj = yzjje * yzjqsy;
			
			//�ʲ���ֵ������˰-�ۼ��۾�
			float zcjz = ufZctz.getZcyzbhs() - zcljzj;
			
//			String update_sql = "update uf_zctz set yzjje = " + yzjje + ",yzjqsy = "+ yzjqsy +
//					",zcljzj = " + zcljzj +",zcjz = " + zcjz + " where id = " + ufZctz.getId();
			String update_sql = "update uf_zctz set yzjje = " + yzjje + ",yzjqsy = "+ yzjqsy +
					",zcljzj = " + zcljzj +",zcjz = " + zcjz + ",ysyqs = " + yzjqsy + " where id = " + ufZctz.getId();
			log.info("�����ʲ�̨�˱�sql -> " + update_sql);
			update_rs.execute(update_sql);
			String inser_sql = "insert into uf_ydzczjb(czny,zclx,zcmc,zcbm,kssyrq,ksyf,czrq,czyf,"
					+ "syr,sybm,zjqsy,czl,yzjl,cgje,zcyzbhs,yzjje,yzjqsy,zcljzj,zcjz,sskf,zcztn,"
					+ "formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime)" + 
			"values('"+DateUtils.getNowStr("yyyy-MM-dd")+"','"+ufZctz.getZclx()+"','" + ufZctz.getId() + "','" + ufZctz.getZcbm() + "','" + 
			ufZctz.getKssyrq()	+ "','" + DateUtils.getMonthByDate(ufZctz.getKssyrq()) + "','" + ufZctz.getZcczrq() + "','" + DateUtils.getMonthByDate(ufZctz.getZcczrq())+ "','" +
			+ ufZctz.getSyr() + "','" + ufZctz.getSybm() + "','" +  ufZctz.getZjqs() + "','" +
			ufZctz.getCzl() + "','" + ufZctz.getYzjl() + "','" + ufZctz.getCgjehs() + "','" + ufZctz.getZcyzbhs() + "','" + 
			+ yzjje + "','" + yzjqsy + "','" + zcljzj + "','" + zcjz + "','" + ufZctz.getSzkf() + "','" + ufZctz.getZczt() + "'," + 
			ydzj_modelid+",1,0,'" + DateUtils.getNowStr("yyyy-MM-dd") + "','" + DateUtils.getNowStr("HH:mm:ss") +"')";
			log.info("�����ʲ�̨�˱�sql -> " + inser_sql);
			insert_rs.execute(inser_sql);
			// 2�������۾��Ƿ����
			if(yzjqsy - zjqs == 0) { // �۾����� = ���۾�����,
				// ���� �۾��Ƿ���� Ϊ ��
				String finish_sql = "update uf_zctz set zjsfwc = 0 where id = " + ufZctz.getId();
				log.info("�����ʲ�̨�˱��۾��Ƿ����sql -> " + update_sql);
				finish_rs.execute(finish_sql);
			}
			
		}
		log.info("�¶��ۿ۶�ʱ����ִ�н���...");
	}
	
}
