package com.techstar.utils;

import io.restassured.path.json.JsonPath;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

/*
 *读取文件的信息
 */
public class GetFileMess {
    private static Properties properties;
    Logger log = Logger.getLogger(GetFileMess.class);

    /*
     *根据properties文件主键获取对应的值
     */
    public String getValue(String key,String propertiesFileName) {
        if(key == null || "".equals(key)) {
            return null;
        }
        properties = new Properties();
        try {
        	InputStream stream = this.getClass().getClassLoader().getResourceAsStream("\\"+propertiesFileName);        
            properties.load(new InputStreamReader(stream,"UTF-8"));
		} catch (IOException e) {
			log.error("配置文件加载失败", e.fillInStackTrace());
		}
        String value = properties.getProperty(key);
        return value;
    }
    /*
     *获取文件路径
     */
    public String getFilePath(String directory,String fileName) {

        try{
            URL resource = this.getClass().getClassLoader().getResource(directory+"/"+fileName);
            String filePath = resource.toURI().getPath();
            log.info("filePath："+filePath);
            return filePath;
        }catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    /*
     *获取测试案例数据的预期结果
     */
    public String getCaseMessKeyValue(String caseMess,String key){
        JsonPath caseMessToJson = new JsonPath(caseMess);
        String value = caseMessToJson.get(key);
        return value;
    }
}
