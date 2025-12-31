package com.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    /**
     * export excel
     * 
     * @param response
     * @param list     Data List (Map)
     * @param headers  Excel Headers
     * @param columns  Map Keys corresponding to headers
     * @param fileName File Name
     * @throws IOException
     */
    public static void export(HttpServletResponse response, List<Map<String, Object>> list, String[] headers,
            String[] columns, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Headers
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> map = list.get(i);
                for (int j = 0; j < columns.length; j++) {
                    Cell cell = row.createCell(j);
                    Object val = map.get(columns[j]);
                    if (val != null) {
                        cell.setCellValue(val.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

        OutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }
}
