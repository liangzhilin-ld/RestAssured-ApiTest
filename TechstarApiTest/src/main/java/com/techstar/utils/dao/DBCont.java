package com.techstar.utils.dao;

import com.techstar.utils.GetFileMess;

/**
 * 数据库常量
 * 
 * @author zhengyanlin
 *
 */
public class DBCont {
	private static String[] DBInfoList = {};
	private static String jdbcConfig = "jdbc.properties";

	public static String[] getDBInfo(String dbName) {
		String DBInfo = new GetFileMess().getValue(dbName, jdbcConfig);
		DBInfoList = DBInfo.split(",");
		return DBInfoList;
	}
}
