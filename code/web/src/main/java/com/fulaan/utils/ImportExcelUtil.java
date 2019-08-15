package com.fulaan.utils;

import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ImportException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caocui on 2015/7/25.
 */
public class ImportExcelUtil {
    private int batchSize;
    private IConvertRow convert;
    private ISaveData saveData;
    private int titleRowNum;

    public interface IConvertRow {
        Object convert(List<String> rowData, List<String> title) throws ImportException;
    }

    public interface ISaveData {
        void save(List data) throws ImportException;
    }

    public ImportExcelUtil(int batchSize, int titleRowNum, IConvertRow convert, ISaveData saveData) {
        this.batchSize = batchSize;
        this.convert = convert;
        this.saveData = saveData;
        this.titleRowNum = titleRowNum;
    }

    private void executeImport(File file) throws ImportException, IllegalParamException, IOException,Exception {
        //创建对Excel工作薄文件的引用
        FileInputStream in = new FileInputStream(file);
        Workbook wb;
        if (file.getName().endsWith(".xls")) {
            wb = new HSSFWorkbook(in);
        } else {
            wb = new XSSFWorkbook(in);
        }
        //创建对工作表的引用
        Sheet sheet = wb.getSheetAt(0);
        //遍历所有单元格，读取单元格
        int row_num = sheet.getLastRowNum();
        int cells = 0;
        List<Object> data = new ArrayList<Object>(this.batchSize <= 0 ? row_num : this.batchSize);
        //获取数据列数
        Row row = sheet.getRow(titleRowNum);
        List<String> title = new ArrayList<String>();
        Cell cell;
        while (true) {
            cell = row.getCell(cells);
            //如果本单元格中没有数据，则停止循环
            if (cell == null || StringUtils.isEmpty(cell.getStringCellValue())) {
                break;
            }
            title.add(cell.getStringCellValue());
            cells++;
        }
        if (title.isEmpty()) {
            throw new IllegalParamException("文件内容错误");
        }
        //解析数据
        List<String> rowData;
        Object obj;
        for (int i = titleRowNum + 1; i <= row_num; i++) {
            row = sheet.getRow(i);
            rowData = new ArrayList<String>(cells);
            for (int j = 0; j <= cells; j++) {
                rowData.add(ImportMethodUtil.readValue(row.getCell(j)));
            }
            obj = this.convert.convert(rowData, title);
            if (obj != null) {
                data.add(obj);
            }

            if (batchSize > 0 && data.size() == batchSize) {
                this.saveData.save(data);
                data.clear();
            }
        }
        if (!data.isEmpty()) {
            this.saveData.save(data);
            data.clear();
        }
    }

    /**
     * 读取文件并且导入数据
     *
     * @param request
     * @param userId    当前用户
     * @param fileField 文件域名称
     * @throws Exception
     */
    public void importData(HttpServletRequest request, String userId, String fileField) throws IllegalParamException, ImportException, IOException ,Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile(fileField);
        String sourceName = multipartFile.getOriginalFilename();
        String base = request.getSession().getServletContext().getRealPath("/") + "attachments" + File.separator + "uploadedExcel";
        File file = new File(base);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = base + File.separator + userId + sourceName;
        File fileImport = null;
        try {
            fileImport = new File(path);
            multipartFile.transferTo(fileImport);
            this.executeImport(new File(path));
        }catch(Exception e){
        	e.printStackTrace();
        	throw e;
        }finally {
            if (fileImport != null && fileImport.exists())
                fileImport.delete();
        }
    }

    /**
     * 导入数据处理类
     */
    public static class ImportMethodUtil {
        //获取单元格的值，如果处理其他类型，扩展此方法
        public static String readValue(Cell cell) {
            if (cell == null) {
                return Constant.EMPTY;
            }
            String strCell;
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    strCell = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    strCell = String.valueOf(cell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    strCell = String.valueOf(cell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    strCell = Constant.EMPTY;
                    break;
                default:
                    strCell = Constant.EMPTY;
                    break;
            }
            if (strCell == null) {
                strCell = Constant.EMPTY;
            }
            return strCell;
        }
    }
}
