package com.change.report.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.format.Alignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.change.report.entities.AssestExportEntity;

public class ExportUtil {


	    public void exportExecl(HttpServletRequest request,HttpServletResponse response, List<AssestExportEntity> importlist, String[] attributeNames,String fileName) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			try {
				String path = exportSurplusMaterialExcel(request, response, importlist,  attributeNames, fileName);
				//4.获取要下载的文件输入流
				InputStream in = new FileInputStream(path);
				int len = 0;
				//5.创建数据缓冲区
				byte[] buffer = new byte[1024];
				//6.通过response对象获取OutputStream流
				OutputStream out;
				out = response.getOutputStream();
				while ((len = in.read(buffer)) > 0) { //7.将FileInputStream流写入到buffer缓冲区
					out.write(buffer,0,len); //8.使用OutputStream将缓冲区的数据输出到客户端浏览器
				}
				in.close();
				out.flush();
				response.flushBuffer();//不可少
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public static String exportSurplusMaterialExcel(HttpServletRequest request,HttpServletResponse response, List<AssestExportEntity> importlist, String[] attributeNames,String fileName) throws IOException, BiffException, WriteException {

	    	String relativePath = "/change/report/zctz/temp/" + fileName + "-" + new Date().getTime() + ".xls";
	    	String pe_templateurl = request.getSession().getServletContext().getRealPath(relativePath);

	    	File file = new File(pe_templateurl);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            WritableWorkbook wbook =  jxl.Workbook.createWorkbook(file);
            WritableSheet writeSheet = wbook.createSheet("sheet1", 0);

	    	WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, false);
	    	WritableCellFormat format = new WritableCellFormat(font);
            format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            format.setAlignment(Alignment.CENTRE);
	    	
            Label label;
	    	int column = 0;//列数计数，写入标题
            for (int i = 0; i < attributeNames.length; i++) {
                label = new Label(column++, 0, attributeNames[i], format);
				writeSheet.addCell(label);
            }
	    	
            for (int i = 0; i < importlist.size(); i++) {
				AssestExportEntity entity = importlist.get(i);
				int r = i + 1;
				label = new Label(0, r, entity.getId().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(1, r, entity.getName(), format);
				writeSheet.addCell(label);
				
				label = new Label(2, r, entity.getV1().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(3, r, entity.getV2().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(4, r, entity.getV3().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(5, r, entity.getV4().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(6, r, entity.getV5().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(7, r, entity.getV6().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(8, r, entity.getV7().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(9, r, entity.getV8().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(10, r, entity.getV9().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(11, r, entity.getV10().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(12, r, entity.getV11().toString(), format);
				writeSheet.addCell(label);
				
				label = new Label(13, r, entity.getV12().toString(), format);
				writeSheet.addCell(label);
			}

	    	wbook.write(); // 写入文件  
	    	wbook.close();
			return relativePath;
	    }

		
}
