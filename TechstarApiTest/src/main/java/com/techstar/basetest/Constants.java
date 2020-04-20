package com.techstar.basetest;

import com.techstar.utils.GetFileMess;
/**
 * 常量配置信息
 * @author liangzhilin
 *
 */
public class Constants {
	public static GetFileMess config=new GetFileMess();
	public static final String CONFIG_JDBC = "jdbc.properties";   
    public static final String CONFIG_COMMON = "config.properties";
    
    public static final String TESTLINK_KEY = config.getValue("testKey", CONFIG_COMMON);
    public static final String TESTLINK_URL = config.getValue("baseURI", CONFIG_COMMON);
    public static final String TESTLINK_TEST_PLAN= config.getValue("testPlan", CONFIG_COMMON);
    public static final String TESTLINK_TEST_PRODUCT = config.getValue("testProduct", CONFIG_COMMON);
    public static final String BASE_URI = config.getValue("baseURI", CONFIG_COMMON);
    public static final String PORT = config.getValue("port", CONFIG_COMMON);
    public static final String VBI_NAME = config.getValue("vbi_name", CONFIG_COMMON);
    public static final String VBI_PWD = config.getValue("vbi_pwd", CONFIG_COMMON);
    public static final String SM_KEY = config.getValue("vbiKey", CONFIG_COMMON);

     
}
