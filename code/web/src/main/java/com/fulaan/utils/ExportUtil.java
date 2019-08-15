package com.fulaan.utils;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Created by Chris on 2015/7/24.
 * ����������
 */
public class ExportUtil {
    private String fileName;
    private final SXSSFWorkbook book;
    private final Sheet sh;
    private int rowCount = 0;

    public ExportUtil() {
        book = new SXSSFWorkbook(100);
        sh = book.createSheet();
        sh.setAutobreaks(true);
    }

    public void addTitle(Object... data) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = book.createFont();
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Row row = sh.createRow(rowCount++);
        String title;
        for (int i = 0, j = data.length; i < j; i++) {
            title = data[i] == null ? "" : data[i].toString();
            Cell cell = row.createCell(i);
            sh.setColumnWidth(i, 3200);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title);
        }
    }

    public void appendColspanRow(List<Map<String,Object>> cols){
    	CellStyle cellStyle = book.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = book.createFont();
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderLeft((short)1);
        cellStyle.setBorderRight((short)1);
        cellStyle.setBorderBottom((short)1);
    	Row row = sh.createRow(rowCount++);
    	int from = 0;
    	//增
    	for(Map<String,Object> col : cols){
    		int start = Integer.parseInt(col.get("s").toString());
    		int end = Integer.parseInt(col.get("e").toString());
    		String value = col.get("v").toString();
    		if(from < start){ 
    			for(int i=from;i<start;i++){
        			Cell cell = row.createCell(i);
        		}
    		}
    		for(int i=start;i<end;i++){
    			Cell cell = row.createCell(i);
        		cell.setCellValue(value);
        		cell.setCellStyle(cellStyle);
    		}
    		from = end;
    	}
    	//合并
    	for(Map<String,Object> col : cols){
    		int start = Integer.parseInt(col.get("s").toString());
    		int end = Integer.parseInt(col.get("e").toString());
    		CellRangeAddress range = new CellRangeAddress(0, 0, start, end - 1);
    		sh.addMergedRegion(range);
    	}
    }
    
    public void appendRow(Object... data) {
        Row row = sh.createRow(rowCount++);
        for (int i = 0, j = data.length; i < j; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i] == null ? "" : data[i].toString());
        }
    }

    public void appendRow(CellStyle cellStyle, Object... data) {
        Row row = sh.createRow(rowCount++);
        for (int i = 0, j = data.length; i < j; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i] == null ? "" : data[i].toString());
            cell.setCellStyle(cellStyle);
        }
    }

    public void appendRowForKW(Object... data) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        Row row = sh.createRow(rowCount++);
        for (int i = 0, j = data.length; i < j; i++) {
            Cell cell = row.createCell(i);
            String value = data[i] == null ? "" : data[i].toString();
            cell.setCellValue(value);
            if(value.equals("等级考")){
                cell.setCellStyle(cellStyle);
            }
        }
    }

    
    /**
     * 根据不同的参数产生想要的Cell格式
     * @param wb  表对象
     * @param fontName  字体
     * @param color  字体颜色
     * @param fontSize  字体大小
     * @param boldWeight  字体加粗
     * @param fillPattern  调整单元格的填充样式
     * @param fillForegroundColor  调整单元格的颜色样式前景色
     * @param align  字体左右居中
     * @param verticel  字体上下居中
     * @param borderTop  设置上边框
     * @param borderBottom  设置下边框
     * @param borderLeft  设置左边框
     * @param borderRight  设置右边框
     * @param wrap  是否自动换行
     */
    public static HSSFCellStyle publicCellStyle(
            HSSFWorkbook wb,
            String fontName,//字体
            String color,//字体颜色
            short fontSize,//字体大小
            String boldWeight,//字体加粗
            String fillPattern,//调整单元格的填充样式
            String fillForegroundColor, //调整单元格的颜色样式前景色
            String align,// 字体左右居中
            String verticel,// 字体上下居中
            String borderTop,//设置上边框
            String borderBottom,//设置下边框
            String borderLeft,//设置左边框
            String borderRight,//设置右边框
            boolean wrap //是否自动换行
){
        HSSFFont font = wb.createFont();
        if (!"".equals(fontName)){
            font.setFontName(fontName);//字体
        }
        if (fontSize!=(short)-1){
            font.setFontHeightInPoints(fontSize);//字体大小
        }
        if("BLACK".equals(color)){
            font.setColor(HSSFColor.BLACK.index);//字体颜色
        }else if("BLUE".equals(color)){
            font.setColor(HSSFColor.BLUE.index);//字体颜色
        }else if("RED".equals(color)){
            font.setColor(HSSFColor.RED.index);//字体颜色
        }else if("".equals(color)){

        }
        if("BOLD".equals(boldWeight)){
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
        }else if("NORMAL".equals(boldWeight)){
            font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//正常字体
        }

        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        if(wrap==true){
            style.setWrapText(true);
        }
        if("CENTER".equals(align)){
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 字体左右居中
        }if("LEFT".equals(align)){
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 字体左右居左
        }if("RIGHT".equals(align)){
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 字体左右居左
        }
        if("CENTER".equals(verticel)){
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 字体上下居中
        }
        if("FOREGROUND".equals(fillPattern)){
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//调整单元格的填充样式
        }
        if("GREY_25_PERCENT".equals(fillForegroundColor)){
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//调整单元格的颜色样式前景色
        }else if("PINK".equals(fillForegroundColor)){
            style.setFillForegroundColor(HSSFColor.PINK.index);//调整单元格的颜色样式前景色
        }else if("ROSE".equals(fillForegroundColor)){
            style.setFillForegroundColor(HSSFColor.ROSE.index);//调整单元格的颜色样式前景色
        }else if("VIOLET".equals(fillForegroundColor)){
            style.setFillForegroundColor(HSSFColor.VIOLET.index);//调整单元格的颜色样式前景色
        }else if ("TITLE".equalsIgnoreCase(fillForegroundColor)) {
            style.setFillForegroundColor((short) 31);
        }
        if("THICK".equals(borderTop)){
            style.setBorderTop(style.BORDER_THICK); // 设置单元格的上边框为粗黑线
        }else if("THIN".equals(borderTop)){
            style.setBorderTop(style.BORDER_THIN); // 设置单元格的上边框为细线
        }
        if("THICK".equals(borderBottom)){
            style.setBorderBottom(style.BORDER_THICK); // 设置单元格的下边框为粗黑线
        }else if("THIN".equals(borderBottom)){
            style.setBorderBottom(style.BORDER_THIN); // 设置单元格的下边框为细线
        }
        if("THICK".equals(borderLeft)){
            style.setBorderLeft(style.BORDER_THICK); // 设置单元格的左边框为粗黑线
        }else if("THIN".equals(borderLeft)){
            style.setBorderLeft(style.BORDER_THIN); // 设置单元格的左边框为细线
        }
        if("THICK".equals(borderRight)){
            style.setBorderRight(style.BORDER_THICK); // 设置单元格的右边框为粗黑线
        }else if("THIN".equals(borderRight)){
            style.setBorderRight(style.BORDER_THIN); // 设置单元格的右边框为细线
        }
        return style;
    }

    public void destroy() {
        if (book != null) {
            book.dispose();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SXSSFWorkbook getBook() {
        return book;
    }

    public Sheet getSheet() {
        return sh;
    }

    public Row getNextRow(){
       return sh.createRow(rowCount++);
    }
}
