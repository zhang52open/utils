package com.froad.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.froad.enums.ResultCode;
import com.froad.exceptions.FroadBusinessException;
import com.froad.ftp.FtpConstants;
import com.froad.ftp.FtpUtil;
import com.froad.logback.LogCvt;

/**
 * excel导出工具类
 * @author 张开
 *
 */
public class ExcelUtil {
	private static final int SPLIT_COUNT = 50000; // Excel每个工作簿的行数
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static long tmpID = 0;
	 
	private static boolean tmpIDlocked = false;

	private static synchronized long getUniqueId() {
		long ltime = 0;
		while (true) {
			if (tmpIDlocked == false) {
				tmpIDlocked = true;
				ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS")
						.format(new Date()).toString()) * 10000;
				if (tmpID < ltime) {
					tmpID = ltime;
				} else {
					tmpID = tmpID + 1;
					ltime = tmpID;
				}
				tmpIDlocked = false;
				return ltime;
			}
		}
	}
	
	/**
	 * 生成Excel-2003   .xls
	 * @param header
	 * @param data
	 * @param sheetName
	 * @return
	 */
	private static HSSFWorkbook generate(List<String> header, List<List<String>> data, String sheetName) {
		long exportBeginTime = System.currentTimeMillis();
		int rows = 0;// 总的记录数
		if (data != null && data.size() > 0) {
			rows = data.size();
		}
		int sheetNum = 0; // 指定sheet的页数
		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}
		try {
			// 生成一个样式
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 创建样式对象
			CellStyle style = workbook.createCellStyle();
			// 设置这些样式
			style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 生成一个字体
			Font font = workbook.createFont();
			font.setColor(HSSFColor.BLACK.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);

			// 生成并设置另一个样式
			CellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			// 生成另一个字体
			Font font2 = workbook.createFont();
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

			// 把字体应用到当前的样式
			style2.setFont(font2);

			// 计算每列的宽度
			Map<Integer, Integer> columWidthMap = autoSetColumnWidth(header, data);
			// 分sheet循环生成work
			
			for (int i = 1; i <= sheetNum+1; i++) {//sheetNum+1为了没数据时能生成空标题
				// 避免有数据时会多生成一个sheet
				if (sheetNum > 0 && i == sheetNum + 1) {
					break;
				}
				// 循环2个sheet的值
				HSSFSheet sheet = workbook.createSheet(sheetName + "-Page" + i);
				// 冻结第一行
				sheet.createFreezePane(0, 1, 0, 1);
				// 使用workbook对象创建sheet对象
				HSSFRow headRow = sheet.createRow((short) 0);
				// 创建行，0表示第一行（本例是excel的标题）
				headRow.setHeight((short) (400));
				// 循环excel的标题
				for (int j = 0; j < header.size(); j++) {
					// 设置列宽
					int width = columWidthMap.get(Integer.valueOf(j));
					if(width > 255*256){
						width = 255*255;
					}
					sheet.setColumnWidth(j, width);
					// 使用行对象创建列对象，0表示第1列
					HSSFCell cell = headRow.createCell(j);
					// cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (header.get(j) != null) {
						// 将创建好的样式放置到对应的单元格中
						/** cell.setCellStyle(cellStyle); */
						cell.setCellValue((String) header.get(j));// 为标题中的单元格设置值
						// 添加样式
						cell.setCellStyle(style);
					} else {
						cell.setCellValue("-");
					}
				}

				// 遍历集合数据，产生数据行
				int count = 0;
				//生成一行空数据
				for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
					if (((i - 1) * SPLIT_COUNT + k) >= rows)
						// 如果数据超出总的记录数的时候，就退出循环
						break;
					// 创建1行
					++count;
					HSSFRow row = sheet.createRow(count);
					// 分页处理，获取每页的结果集，并将数据内容放入excel单元格
					List<String> rowList = (List<String>) data.get((i - 1) * SPLIT_COUNT + k);
					// 遍历某一行的结果
					for (int n = 0; n < rowList.size(); n++) {
						// 使用行创建列对象
						HSSFCell cell = row.createCell(n);
						if (rowList.get(n) != null) {
							cell.setCellValue((String) rowList.get(n).toString());
						} else {
							cell.setCellValue("");
						}
					}
				}
			}
			long exportEndTime = System.currentTimeMillis();
			LogCvt.info("生成Excel成功，耗时 : " + (exportEndTime - exportBeginTime) + "毫秒");
			return workbook;
		} catch (Exception e) {
			LogCvt.error("生成Excel异常：",e);
		}

		return null;
	}

	/**
	 * 
	 * @param title
	 * @param subTitle
	 * @param header
	 * @param data
	 * @param foot
	 * @return
	 */
	private static SXSSFWorkbook generate(List<String> header, List<List<String>> data, String sheetName,boolean isAppend) {
		long exportBeginTime = System.currentTimeMillis();
		int rows = 0;// 总的记录数
		if (data != null && data.size() > 0) {
			rows = data.size();
		}
		int sheetNum = 0; // 指定sheet的页数
		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}
		try {
			// 生成一个样式
			SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
			// 创建样式对象
			CellStyle style = workbook.createCellStyle();
			// 设置这些样式
			style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 生成一个字体
			Font font = workbook.createFont();
			font.setColor(HSSFColor.BLACK.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);

			// 生成并设置另一个样式
			CellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			// 生成另一个字体
			Font font2 = workbook.createFont();
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

			// 把字体应用到当前的样式
			style2.setFont(font2);

			// 计算每列的宽度
			Map<Integer, Integer> columWidthMap = autoSetColumnWidth(header, data);
			// 分sheet循环生成work
			
			for (int i = 1; i <= sheetNum+1; i++) {//sheetNum+1为了没数据时能生成空标题
				// 避免有数据时会多生成一个sheet
				if (sheetNum > 0 && i == sheetNum + 1) {
					break;
				}
				// 循环2个sheet的值
				Sheet sheet = workbook.createSheet(sheetName + "-Page" + i);
				// 冻结第一行
				sheet.createFreezePane(0, 1, 0, 1);
				// 使用workbook对象创建sheet对象
				Row headRow = sheet.createRow((short) 0);
				// 创建行，0表示第一行（本例是excel的标题）
				headRow.setHeight((short) (400));
				// 循环excel的标题
				for (int j = 0; j < header.size(); j++) {
					// 设置列宽
					sheet.setColumnWidth(j, columWidthMap.get(Integer.valueOf(j)));
					// 使用行对象创建列对象，0表示第1列
					Cell cell = headRow.createCell(j);
					// cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (header.get(j) != null) {
						// 将创建好的样式放置到对应的单元格中
						/** cell.setCellStyle(cellStyle); */
						cell.setCellValue((String) header.get(j));// 为标题中的单元格设置值
						// 添加样式
						cell.setCellStyle(style);
					} else {
						cell.setCellValue("-");
					}
				}

				// 遍历集合数据，产生数据行
				int count = 0;
				//生成一行空数据
				for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
					if (((i - 1) * SPLIT_COUNT + k) >= rows)
						// 如果数据超出总的记录数的时候，就退出循环
						break;
					// 创建1行
					++count;
					Row row = sheet.createRow(count);
					// 分页处理，获取每页的结果集，并将数据内容放入excel单元格
					List<String> rowList = (List<String>) data.get((i - 1) * SPLIT_COUNT + k);
					// 遍历某一行的结果
					for (int n = 0; n < rowList.size(); n++) {
						// 使用行创建列对象
						Cell cell = row.createCell(n);
						if (rowList.get(n) != null) {
							cell.setCellValue((String) rowList.get(n).toString());
						} else {
							cell.setCellValue("");
						}
					}
				}
			}
			long exportEndTime = System.currentTimeMillis();
			LogCvt.info("生成Excel成功，耗时 : " + (exportEndTime - exportBeginTime) + "毫秒");
			return workbook;
		} catch (Exception e) {
			LogCvt.error("生成Excel异常：",e);
		}

		return null;
	}
	
	private static XSSFWorkbook append(String excelLocalFilePath,List<String> header, List<List<String>> data, String sheetName) {
		long exportBeginTime = System.currentTimeMillis();
		int rows = 0;// 总的记录数
		if (data != null && data.size() > 0) {
			rows = data.size();
		}
		int sheetNum = 0; // 指定sheet的页数
		if (rows % SPLIT_COUNT == 0) {
			sheetNum = rows / SPLIT_COUNT;
		} else {
			sheetNum = rows / SPLIT_COUNT + 1;
		}
		try {
			// 生成一个样式
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelLocalFilePath));
			XSSFWorkbook workbook = new XSSFWorkbook(bis);
			// 创建样式对象
			CellStyle style = workbook.createCellStyle();
			// 设置这些样式
			style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 生成一个字体
			Font font = workbook.createFont();
			font.setColor(HSSFColor.BLACK.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);

			// 生成并设置另一个样式
			CellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			// 生成另一个字体
			Font font2 = workbook.createFont();
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

			// 把字体应用到当前的样式
			style2.setFont(font2);

			// 计算每列的宽度
			Map<Integer, Integer> columWidthMap = autoSetColumnWidth(header, data);
			// 分sheet循环生成work
			
			for (int i = 1; i <= sheetNum+1; i++) {//sheetNum+1为了没数据时能生成空标题
				// 避免有数据时会多生成一个sheet
				if (sheetNum > 0 && i == sheetNum + 1) {
					break;
				}
				// 循环2个sheet的值
				Sheet sheet = workbook.createSheet(sheetName + "-Page" + workbook.getNumberOfSheets()+i);
				// 使用workbook对象创建sheet对象
				Row headRow = sheet.createRow((short) 0);
				// 创建行，0表示第一行（本例是excel的标题）
				headRow.setHeight((short) (400));
				// 循环excel的标题
				for (int j = 0; j < header.size(); j++) {
					// 设置列宽
					sheet.setColumnWidth(j, columWidthMap.get(Integer.valueOf(j)));
					// 使用行对象创建列对象，0表示第1列
					Cell cell = headRow.createCell(j);
					// cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (header.get(j) != null) {
						// 将创建好的样式放置到对应的单元格中
						/** cell.setCellStyle(cellStyle); */
						cell.setCellValue((String) header.get(j));// 为标题中的单元格设置值
						// 添加样式
						cell.setCellStyle(style);
					} else {
						cell.setCellValue("-");
					}
				}

				// 遍历集合数据，产生数据行
				int count = 0;
				//生成一行空数据
				for (int k = 0; k < (rows < SPLIT_COUNT ? rows : SPLIT_COUNT); k++) {
					if (((i - 1) * SPLIT_COUNT + k) >= rows)
						// 如果数据超出总的记录数的时候，就退出循环
						break;
					// 创建1行
					++count;
					Row row = sheet.createRow(count);
					// 分页处理，获取每页的结果集，并将数据内容放入excel单元格
					List<String> rowList = (List<String>) data.get((i - 1) * SPLIT_COUNT + k);
					// 遍历某一行的结果
					for (int n = 0; n < rowList.size(); n++) {
						// 使用行创建列对象
						Cell cell = row.createCell(n);
						if (rowList.get(n) != null) {
							cell.setCellValue((String) rowList.get(n).toString());
						} else {
							cell.setCellValue("");
						}
					}
				}
			}
			long exportEndTime = System.currentTimeMillis();
			LogCvt.info("生成Excel成功，耗时 : " + (exportEndTime - exportBeginTime) + "毫秒");
			return workbook;
		} catch (Exception e) {
			LogCvt.error("生成Excel异常：",e);
		}

		return null;
	}

	private static String getFileName() {
		return ExcelUtil.format.format(new Date()) + Math.round((Math.random() * 10)) + ".xls";
	}

	private static String getFileShortName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date()) + Math.round((Math.random() * 10));
	}

	private static String getDefaultFoot() {
		String foot = "制表人：admin";
		return foot;
	}

	/**
	 * 自动设置列宽
	 */
	private static Map<Integer, Integer> autoSetColumnWidth(List<String> header, List<List<String>> data) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (header != null) {
			for (int i = 0; i < header.size(); i++) {
				map.put(i, header.get(i).getBytes().length * 1 * 256);
			}
		}
		if (data != null) {
			for (List<String> results : data) {
				for (int i = 0; i < results.size(); i++) {
					if (results.get(i) != null) {
						int result = results.get(i).getBytes().length * 1 * 256;
						if (map.get(i) < result) {
							map.put(i, result);
						}
					} else {
						int result = 0;
						if (map.get(i) < result) {
							map.put(i, result);
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * @param header 表头名称
	 * @param data 数据
	 * @param sheetName sheet名称
	 * @return url
	 * @throws FroadBusinessException
	 */
	public static String export(List<String> header, List<List<String>> data, String sheetName) throws FroadBusinessException{
		return export(header, data, sheetName,null);
	}
	
	/**
	 * 将Excel写到本地目录
	 * @param workbook Excel工作簿
	 * @param localFilePath 本地服务器路径
	 * @throws FroadBusinessException
	 */
	private static void writeFile(HSSFWorkbook workbook,String localFilePath) throws FroadBusinessException{
		long startTime2 = System.currentTimeMillis();
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(localFilePath));
			workbook.write(bos);
			bos.flush();
		} catch (FileNotFoundException e) {
			LogCvt.error("Excel导出异常:",e);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"导出失败");
		} catch (IOException e) {
			LogCvt.error("Excel导出异常:",e);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"导出失败");
		} finally {
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					LogCvt.error("关闭OutputStream异常:",e);
				}
			}
		}
		long endTime2 = System.currentTimeMillis();
		LogCvt.info("EXCEL写本地文件耗时："+(endTime2-startTime2)+"ms");
	}
	
	/**
	 * 将Excel写到本地目录
	 * @param workbook Excel工作簿
	 * @param localFilePath 本地服务器路径
	 * @throws FroadBusinessException
	 */
	private static void writeFile(SXSSFWorkbook workbook,String localFilePath) throws FroadBusinessException{
		long startTime2 = System.currentTimeMillis();
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(localFilePath));
			workbook.write(bos);
			bos.flush();
		} catch (FileNotFoundException e) {
			LogCvt.error("Excel导出异常:",e);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"导出失败");
		} catch (IOException e) {
			LogCvt.error("Excel导出异常:",e);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"导出失败");
		} finally {
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					LogCvt.error("关闭OutputStream异常:",e);
				}
			}
		}
		long endTime2 = System.currentTimeMillis();
		LogCvt.info("EXCEL写本地文件耗时："+(endTime2-startTime2)+"ms");
	}
	
	/**
	 * @param header 表头名称
	 * @param data 数据
	 * @param sheetName sheet名称
	 * @param excelFileName excel文件名
	 * @return url
	 * @throws FroadBusinessException
	 */
	public static String export(List<String> header, List<List<String>> data, String sheetName,String excelFileName) throws FroadBusinessException{
		long startTime = System.currentTimeMillis();
		//校验
		if (header == null || header.size() <= 0) {
			throw new FroadBusinessException(ResultCode.failed.getCode(),"Excel标题栏不能为空");
		}
		if (data == null || data.size() <= 0) {
			throw new FroadBusinessException(ResultCode.failed.getCode(),"Excel数据不能为空");
		}
		if (header.size() != data.get(0).size()) {
			throw new FroadBusinessException(ResultCode.failed.getCode(),"Excel标题栏和数据栏列数不一致：标题栏为"+header.size()+"列，数据为"+data.get(0).size()+"列");
		}
		
		//导出URL
		String url = "";
		
		HSSFWorkbook workbook = generate(header, data, sheetName);
//		SXSSFWorkbook workbook = generate(header, data, sheetName,true);
		String fileName = getFileName();
		if(StringUtils.isNotEmpty(excelFileName)){
			fileName = excelFileName + "-" + getFileName();
		}
		
		String localFileDir = FtpUtil.SFTP_PROPERTIES.get(FtpConstants.LOCAL_FILE_DIR);
		if(StringUtils.isEmpty(localFileDir)){
			LogCvt.error("未找到配置项：（本地导出目录）"+FtpConstants.LOCAL_FILE_DIR);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"导出失败");
		}
		File fileDir = new File(localFileDir);
		if(!fileDir.exists()){
			if(!fileDir.mkdirs()){
				LogCvt.error("创建本地目录失败！"+localFileDir);
				throw new FroadBusinessException(ResultCode.failed.getCode(),"导出失败");
			}
		}
		
		String localFilePath = FtpUtil.SFTP_PROPERTIES.get(FtpConstants.LOCAL_FILE_DIR) + FtpConstants.SLASH + fileName;
		
		writeFile(workbook,localFilePath);
		
		url = FtpUtil.upload(fileName);
		
		File file = new File(localFilePath);
		file.delete();
        
        long endTime = System.currentTimeMillis();
        LogCvt.info("EXCEL导出总耗时："+(endTime-startTime)+"ms");
		
		return url;
	}
	
}
