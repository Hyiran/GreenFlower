package org.gradle.needle.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gradle.needle.dbo.GlobalSettings;

public class HttpResVer {
	private static Logger logger = Logger.getLogger(HttpResVer.class.getName());

	// 定义几个列常量，用于数据读取或写入。(这个地方写的烂，还要改)
	static int responsecol = GlobalSettings.response;
	static int expectcol = GlobalSettings.expect;
	static int E_keycol = GlobalSettings.E_key;
	static int E_valuecol = GlobalSettings.E_value;
	static int A_keycol = GlobalSettings.A_key;
	static int A_valuecol = GlobalSettings.A_value;
	static int Resultcol = GlobalSettings.Result;

	/**
	 * 对基线expect和返回response做循环对比，将对比结果写入comparison并标记
	 * 
	 * @param testid
	 * @param bsheet
	 * @param response
	 * @param csheet
	 */
	public static void ParserAndCompare(String testid, String bsheet,
			String response, String csheet) {

		Map<String, String> actualmap = parser(response);
		try {
			// 在bsheet中读取expect字符串
			ExcelDataUtils.setExcelWorkSheet(bsheet);
			int brownum = ExcelDataUtils.getRowNumberOnTestid(testid);
			String expectstr = (String) ExcelDataUtils.getCellData(brownum,
					expectcol);

			// 切换至csheet,写入对比后结果
			ExcelDataUtils.setExcelWorkSheet(csheet);
			int crownum = ExcelDataUtils.getRowNumberOnTestid(testid);
			Map<String, String> expectmap = parser(expectstr);
			Iterator<String> iter = expectmap.keySet().iterator();

			while (iter.hasNext()) {
				String ekey = iter.next();
				String evalue = expectmap.get(ekey);
				ExcelDataUtils.setCellData(ekey, crownum, E_keycol);
				ExcelDataUtils.setBorder(crownum, E_keycol);
				ExcelDataUtils.setCellData(evalue, crownum, E_valuecol);
				ExcelDataUtils.setBorder(crownum, E_valuecol);

				String avalue = actualmap.get(ekey);
				if (avalue != null) {
					ExcelDataUtils.setCellData(ekey, crownum, A_keycol);
					ExcelDataUtils.setBorder(crownum, A_keycol);
					ExcelDataUtils.setCellData(avalue, crownum, A_valuecol);
					ExcelDataUtils.setBorder(crownum, A_valuecol);
					if (avalue.equals(evalue)) {
						ExcelDataUtils
								.setCellData("passed", crownum, Resultcol);
						ExcelDataUtils.setBorder(crownum, Resultcol);
						ExcelDataUtils
								.setCellColor("GREEN", crownum, Resultcol);
					} else {
						ExcelDataUtils
								.setCellData("failed", crownum, Resultcol);
						ExcelDataUtils.setBorder(crownum, Resultcol);
						ExcelDataUtils.setCellColor("YELLOW", crownum,
								Resultcol);
					}
				} else {
					ExcelDataUtils.setCellData("--", crownum, A_keycol);
					ExcelDataUtils.setCellData("--", crownum, A_valuecol);
					ExcelDataUtils.setCellData("failed", crownum, Resultcol);
					ExcelDataUtils.setBorder(crownum, Resultcol);
					ExcelDataUtils.setCellColor("YELLOW", crownum, Resultcol);
					logger.info(testid + ":  " + ekey + "该统计量在返回结果中不存在");
				}

				// 这里在testid行下面插入一个新行，准备写入下一组key-value
				int totalrows = ExcelDataUtils.getSheet().getLastRowNum(); // 获取总行数
				ExcelDataUtils.insertRow(crownum, totalrows, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析接口请求后的字符串，将变量和值存入map
	 * 
	 * @param response
	 * @return
	 */
	private static Map<String, String> parser(String response) {
		Map<String, String> m = new HashMap<String, String>();

		// 对所解析的字符串做校验，排除返回结果为空或返回非正常的结果
		if (response != null) {
			if (response.indexOf("OK") >= 0) {
				String[] tokens = response.split("},");
				for (int i = 0; i < tokens.length; i++) {
					int firstindex = tokens[i].indexOf("\"");
					int lastindex = tokens[i].lastIndexOf("\"");
					String key = tokens[i].substring(firstindex + 1,
							tokens[i].indexOf("\"", firstindex + 1) - 1);
					String value = tokens[i].substring(
							tokens[i].lastIndexOf("\"", lastindex - 1) + 1,
							lastindex - 1);
					m.put(key, value);
				}
			} else {
				logger.info("非正常返回结果，不予以解析");
			}
		} else {
			logger.info("返回结果为空，不予以解析");
		}
		return m;
	}

	/**
	 * 将接口的返回结果写入output中
	 * 
	 * @param osheet
	 * @param testid
	 * @param response
	 */
	public static void saveResponse(String osheet, String testid,
			String response) {
		try {
			ExcelDataUtils.setExcelWorkSheet(osheet);
			int rownumber = ExcelDataUtils.getRowNumberOnTestid(testid);
			ExcelDataUtils.setCellData(response, rownumber, responsecol);
			ExcelDataUtils.setBorder(rownumber, responsecol);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 对外部测试集EXCEL进行清理
	 * 
	 * @param testid
	 * @param sheet
	 */
	public static void teardown(String testid, String sheet) {

	}
}
