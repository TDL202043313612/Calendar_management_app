package cn.wujiangbo.utils.poi;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 功能说明 excel导入导出工具类
 *
 */
public class ExcelReadUtil<T> {

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出
     * <p>
     * title         表格标题名
     * headersName  表格属性列名数组
     * headersId    表格属性列名对应的字段---你需要导出的字段名（为了更灵活控制你想要导出的字段）
     * dtoList     需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象
     * out         与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public byte[] exportExcel(String title, List<String> headersName, List<String> headersId,
                              List<T> dtoList) {
        /*（一）表头--标题栏*/
        Map<Integer, String> headersNameMap = new HashMap<>();
        int key = 0;
        for (int i = 0; i < headersName.size(); i++) {
            if (!headersName.get(i).equals(null)) {
                headersNameMap.put(key, headersName.get(i));
                key++;
            }
        }
        /*（二）字段*/
        Map<Integer, String> titleFieldMap = new HashMap<>();
        int value = 0;
        for (int i = 0; i < headersId.size(); i++) {
            if (!headersId.get(i).equals(null)) {
                titleFieldMap.put(value, headersId.get(i));
                value++;
            }
        }
        /* （三）声明一个工作薄：包括构建工作簿、表格、样式*/
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(title);
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        HSSFRow row = sheet.createRow(0);
        // style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell;
        Collection c = headersNameMap.values();//拿到表格所有标题的value的集合
        Iterator<String> it = c.iterator();//表格标题的迭代器
        /*（四）导出数据：包括导出标题栏以及内容栏*/
        //根据选择的字段生成表头
        short size = 0;
        while (it.hasNext()) {
            cell = row.createCell(size);
            cell.setCellValue(it.next().toString());
            cell.setCellStyle(style);
            size++;
        }
        //表格标题一行的字段的集合
        Collection zdC = titleFieldMap.values();
        Iterator<T> labIt = dtoList.iterator();//总记录的迭代器
        int zdRow = 0;//列序号
        while (labIt.hasNext()) {//记录的迭代器，遍历总记录
            int zdCell = 0;
            zdRow++;
            row = sheet.createRow(zdRow);
            T l = (T) labIt.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = l.getClass().getDeclaredFields();//获得JavaBean全部属性
            for (short i = 0; i < fields.length; i++) {//遍历属性，比对
                Field field = fields[i];
                String fieldName = field.getName();//属性名
                Iterator<String> zdIt = zdC.iterator();//一条字段的集合的迭代器
                while (zdIt.hasNext()) {//遍历要导出的字段集合
                    if (zdIt.next().equals(fieldName)) {//比对JavaBean的属性名，一致就写入，不一致就丢弃
                        String getMethodName = "get"
                                + fieldName.substring(0, 1).toUpperCase()
                                + fieldName.substring(1);//拿到属性的get方法
                        Class tCls = l.getClass();//拿到JavaBean对象
                        try {
                            Method getMethod = tCls.getMethod(getMethodName,
                                    new Class[]{});//通过JavaBean对象拿到该属性的get方法，从而进行操控
                            Object val = getMethod.invoke(l, new Object[]{});//操控该对象属性的get方法，从而拿到属性值
                            String textVal = null;
                            if (val != null) {
                                textVal = String.valueOf(val);//转化成String
                            } else {
                                textVal = null;
                            }
                            row.createCell((short) zdCell).setCellValue(textVal);//写进excel对象
                            zdCell++;
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return wb.getBytes();
    }

    public static List<List<String>> readExcel(InputStream is, int beginRow) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return readExcel(is, sf, beginRow);
    }

    /**
     * 功能说明  excel数据导入
     *
     * @param is       读取的excel文件流
     * @param sf       日期类型的格式化格式
     * @param beginRow 从第几行开始读取数据
     * @return java.util.List<java.util.List < java.lang.String>>
     * @author caiwen
     * @date 2022/8/11
     */
    public static List<List<String>> readExcel(InputStream is, SimpleDateFormat sf, int beginRow) throws IOException {
        Workbook wb = null;

        try {
            wb = WorkbookFactory.create(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** 得到第一个sheet */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        int totalRows = sheet.getPhysicalNumberOfRows();

        /** 得到Excel的列数 */
        int totalCells = 0;
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        List<List<String>> dataLst = new ArrayList<List<String>>();
        /** 循环Excel的行 ，默认从第2行开始循环，首页默认是表头*/
        for (int r = beginRow; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            if (isRowEmpty(row)) continue; //过滤空行
            List<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                     /*HSSFDataFormatter hSSFDataFormatter = new HSSFDataFormatter();
                     cellValue= hSSFDataFormatter.formatCellValue(cell);*/


                    // 以下是判断数据的类型
                    CellType type = cell.getCellTypeEnum();

                    switch (type) {
                        case NUMERIC: // 数字
                            //判断是否是日期格式
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                                cellValue = sf.format(javaDate);
                            } else {
                                cellValue = cell.getNumericCellValue() + "";
                            }
//                            //转换时间格式
//                            if (r != 0 && c == 7) {
//                                Date date = dateFormat(cellValue.substring(0, cellValue.length() - 2));
////                                System.out.println(date);
//                                cellValue = sf.format(date);
//                            }
//                            //校验整数
//                            if (r != 0 && c == 6) {
//                                if (cellValue.contains(".")) {
//                                    int i = cellValue.indexOf(".");
//                                    cellValue = cellValue.substring(0, i);
//                                }
//
//                            }
                            break;
                        case STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case FORMULA: // 公式
                            try {
                                cellValue = cell.getStringCellValue();
                            } catch (IllegalStateException e) {
                                cellValue = String.valueOf(cell.getNumericCellValue());
                            }
                            break;
                       /* cellValue = cell.getCellFormula() + "";
                        break;*/
                        case BLANK: // 空值
                            cellValue = "";
                            break;
                        case _NONE: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }

                rowLst.add(cellValue);
            }
            /** 保存第r行的第c列 */
            dataLst.add(rowLst);
        }
        return dataLst;
    }


    //时间转换方法 传取出来的时间数字
    public static Date dateFormat(String conStart1) {
        Calendar calendar = new GregorianCalendar(1900, 0, -1);
        Date d = calendar.getTime();
        Date dd = DateUtils.addDays(d, Integer.valueOf(conStart1));
        return dd;
    }

    //判断是否空行
    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null)
                return false;
        }
        return true;
    }

}

