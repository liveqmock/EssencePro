package com.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.model.ExeclBean;

@SuppressWarnings("unchecked")
public class ExcelTool<T> {

    private static final int COLUMN_WIDTH = 15;

    private static ExcelTool instance;

    private ExcelTool() {
    }

    // 单例模式——1、构造方法私有化2、私有静态变量3、公共静态方法getInstance
    public static ExcelTool getInstance() {
        if (null == instance)
            instance = new ExcelTool();
        return instance;
    }

    /**
     * 导出方法
     * 
     * @param title
     *            ：标题
     * @param herders
     *            封装了ExeclBean类的list，用于循环输出表头信息
     * @param ：数据源，实现了Collection接口的任意集合类
     * @param out
     *            ：OutputStream对象
     * @throws Exception
     */
    public void exportExecl(String title, List<ExeclBean> herders, Collection<T> ds, OutputStream out) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(title);
        sheet.setDefaultColumnWidth(COLUMN_WIDTH);
        // 设置title
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(getTitleStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, herders.size() - 1));
        // 设置制单日期
        HSSFRow dateRow = sheet.createRow(1);
        if (herders.size() == 1) {
            HSSFCell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("制单日期：" + DateUtil.stringOfCnDate());
        } else {
            dateRow.createCell(herders.size() - 2).setCellValue("制单日期：");
            dateRow.createCell(herders.size() - 1).setCellValue(DateUtil.stringOfCnDate());
        }
        // 设置表头header
        HSSFRow row = sheet.createRow(2);
        HSSFCellStyle headerSytle = getHeaderStyle(workbook);
        HSSFCellStyle contentSyle = getContentStyle(workbook);
        // 循环表头，设置样式
        for (int i = 0; i < herders.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(headerSytle);
            HSSFRichTextString text = new HSSFRichTextString(herders.get(i).getName());
            cell.setCellValue(text);
        }

        Iterator<T> it = ds.iterator();
        int idx = 2;
        String getMethod = "";
        // 循环表体
        while (it.hasNext()) {
            idx++;

            row = sheet.createRow(idx);

            T t = it.next();

            Class tClass = t.getClass();

            String textValue = "";

            for (int i = 0; i < herders.size(); i++) {

                HSSFCell cell = row.createCell(i);

                cell.setCellStyle(contentSyle);

                getMethod = herders.get(i).getGetterMethod();

                // 通过反射得到get方法
                Method getter = tClass.getMethod(getMethod, null);
                // 通过反射执行get方法
                Object value = getter.invoke(t, null);

                if (null == value)
                    value = "";

                // 处理日期类型值的显示方法
                if (value instanceof Date) {
                    Date date = (Date) value;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    textValue = sdf.format(date);
                } else {
                    textValue = value.toString();
                }

                // 处理数字类型值的显示方法
                Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");

                Matcher matcher = p.matcher(textValue);

                if (matcher.matches()) {

                    // 是数字当作double处理

                    cell.setCellValue(Double.parseDouble(textValue));

                } else {

                    HSSFRichTextString richString = new HSSFRichTextString(

                    textValue);

                    HSSFFont font3 = workbook.createFont();

                    font3.setColor(HSSFColor.BLUE.index);

                    richString.applyFont(font3);

                    cell.setCellValue(richString);

                }
            }
        }

        // 向outputstream对象中输出
        workbook.write(out);

    }

    /**
     * 导出方法
     * 
     * @param title
     *            ：标题
     * @param herders
     *            封装了ExeclBean类的list，用于循环输出表头信息
     * @param ds
     *            ：数据源，实现了Collection接口的任意集合类
     * @param out
     *            ：OutputStream对象
     * @throws Exception
     */
    public void exportExecl(String title, List<ExeclBean> herders, Collection<T> ds, Map<String, String> others, OutputStream out)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet(title);

        sheet.setDefaultColumnWidth(COLUMN_WIDTH);

        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(getTitleStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, herders.size() - 1));

        HSSFRow dateRow = sheet.createRow(1);
        if (herders.size() == 1) {
            HSSFCell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("制表日期：" + DateUtil.stringOfCnDate());
        } else {
            dateRow.createCell(herders.size() - 2).setCellValue("制表日期：");
            dateRow.createCell(herders.size() - 1).setCellValue(DateUtil.stringOfCnDate());
        }

        HSSFRow row = sheet.createRow(2);

        HSSFCellStyle headerSytle = getHeaderStyle(workbook);

        HSSFCellStyle contentSyle = getContentStyle(workbook);

        // 循环表头，设置样式
        for (int i = 0; i < herders.size(); i++) {

            HSSFCell cell = row.createCell(i);

            cell.setCellStyle(headerSytle);

            HSSFRichTextString text = new HSSFRichTextString(herders.get(i).getName());

            cell.setCellValue(text);

        }

        Iterator<T> it = ds.iterator();

        int idx = 2;

        String getMethod = "";

        // 循环表体
        while (it.hasNext()) {
            idx++;

            row = sheet.createRow(idx);

            T t = it.next();

            Class tClass = t.getClass();

            String textValue = "";

            for (int i = 0; i < herders.size(); i++) {

                HSSFCell cell = row.createCell(i);

                cell.setCellStyle(contentSyle);

                getMethod = herders.get(i).getGetterMethod();

                // 通过反射得到get方法
                Method getter = tClass.getMethod(getMethod, null);
                // 通过反射执行get方法
                Object value = getter.invoke(t, null);

                if (null == value)
                    value = "";

                // 处理日期类型值的显示方法
                if (value instanceof Date) {
                    Date date = (Date) value;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    textValue = sdf.format(date);
                } else {
                    textValue = value.toString();
                }

                // 处理数字类型值的显示方法
                Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");

                Matcher matcher = p.matcher(textValue);

                if (matcher.matches()) {

                    // 是数字当作double处理

                    cell.setCellValue(Double.parseDouble(textValue));

                } else {

                    HSSFRichTextString richString = new HSSFRichTextString(textValue);

                    HSSFFont font3 = workbook.createFont();

                    font3.setColor(HSSFColor.BLUE.index);

                    richString.applyFont(font3);

                    cell.setCellValue(richString);

                }
            }
        }

        idx++;
        Iterator<String> oit = others.keySet().iterator();
        while (oit.hasNext()) {
            idx++;
            String key = oit.next();
            String value = others.get(key);

            row = sheet.createRow(idx);
            HSSFCell keyCell = row.createCell(0);
            keyCell.setCellValue(key);
            HSSFCell valueCell = row.createCell(1);
            valueCell.setCellValue(value);

        }

        // 向outputstream对象中输出
        workbook.write(out);

    }

    // 设置表头样式，可自定义
    public HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFFont font = workbook.createFont();

        font.setColor(HSSFColor.VIOLET.index);

        font.setFontHeightInPoints((short) 12);

        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        style.setFont(font);

        return style;
    }

    public HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFFont font = workbook.createFont();

        font.setColor(HSSFColor.BLACK.index);

        font.setFontHeightInPoints((short) 16);

        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        style.setFont(font);

        return style;
    }

    // 设置表体样式，可自定义
    public HSSFCellStyle getContentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style2 = workbook.createCellStyle();

        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        HSSFFont font2 = workbook.createFont();

        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        style2.setFont(font2);

        return style2;
    }

    /**
     * 把excel文件转换为list<T>,只能转换sheet1.
     * 思路：获取workbook——获取sheet——遍历sheet每一行——把每一行转换为一个map——把map转换为model。
     * 
     * @param filePath
     * @param headers
     *            格式类似 String[] headers = { "id", "bm", "name" };
     * @return
     * @author 玄承勇
     * @date 2014-5-14 下午4:53:56
     */
    //

    public static <T> List<T> transformCollectionByFile(String filePath, Class<T> t, String[] headers) {
        List<T> modellist = new ArrayList<T>();
        try {
            Workbook wb = Workbook.getWorkbook(new File(filePath));
            Sheet sheet = wb.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++) {
                T model = getModel(sheet, headers, i, t);
                modellist.add(model);
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modellist;
    }

    /**
     * 把sheet的规定一行row转换为map.
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-16 下午9:30:51
     * @param:
     * @param:
     * @return ： Map<String,Object>
     */
    private static Map<String, Object> getMap(Sheet sheet, String[] fields, int row) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < fields.length; i++) {
            // 行列确定一个cell
            Cell cell = sheet.getCell(i, row);
            // 获取cell中想要的值——字符串
            String value = getCellValueStr(cell);

            map.put(fields[i].toLowerCase(), value);
        }
        return map;
    }

    /**
     * 获取cell中的值（字符串）
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-16 下午9:31:38
     * @param:
     * @param:
     * @return ： String
     */
    private static String getCellValueStr(Cell cell) {
        if (cell.getType() == CellType.DATE) {
            DateCell dateCell = (DateCell) cell;
            return new SimpleDateFormat("yyyy-MM-dd").format(dateCell.getDate());
        } else {
            return cell.getContents();
        }
    }

    /**
     * 根据sheet获取model
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-16 下午9:32:52
     * @param:
     * @param:
     * @return ： T
     */
    private static <T> T getModel(Sheet sheet, String[] fields, int row, Class<T> modelType) {
        Map<String, Object> map = getMap(sheet, fields, row);

        return ClassUtil.map2model(map, modelType);
    }

}
