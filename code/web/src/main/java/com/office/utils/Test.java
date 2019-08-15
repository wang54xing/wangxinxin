package com.office.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Test {

	public static void main(String[] args) throws Exception {
  
        Map<String, Object> params = new HashMap<String, Object>();  
  
        params.put("${zhongdian}", "<font size=22px>大幅拉升的里发了啥地方叫骄傲的是爱的发的发生按时大大发的发生的阿萨德法师打发第三方阿斯顿发的说法是对方爱的发生的发达阿斯顿发斯蒂芬</font>");  
        params.put("${niandian}", "大幅拉升的里发了啥地方叫骄傲的是爱的发的发生按时大大发的发生的阿萨德法师打发第三方阿斯顿发的说法是对方爱的发生的发达阿斯顿发斯蒂芬");  

  
        XwpfTUtil xwpfTUtil = new XwpfTUtil();  
  
        InputStream  is = new FileInputStream("d:\\123.docx");
        XWPFDocument  doc = new XWPFDocument(is);  
  
       
        xwpfTUtil.replaceInPara(doc, params);  
        //替换表格里面的变量  
        xwpfTUtil.replaceInTable(doc, params);  
        OutputStream os = new FileOutputStream("d:\\456.docx");
  
    
  
        doc.write(os);  
  
        xwpfTUtil.close(os);  
        xwpfTUtil.close(is);  
  
        os.flush();  
        os.close();  
	}
}
