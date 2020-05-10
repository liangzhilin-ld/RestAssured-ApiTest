package com.techstar.basetest;
import com.techstar.datatransfer.DataProviders;
import com.techstar.datatransfer.TestLinkCase;
//import com.iiaccount.dao.ExcutSqlFile;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/*
 *父类的注解可以被子类继承，所有的环境变量在父类进行配置
 *根据testng.xml文件传入的参数来选择环境参数，默认为yg_env
 */
public class SetUpTearDown extends Constants {
	private static Logger log = Logger.getLogger(RunCaseAPI.class);
	public static HashMap<String,List<TestLinkCase>> allCase;
	public String token;
    @BeforeSuite
    public void dataSetUp() throws SQLException, IOException, ClassNotFoundException {
    	//初始化获取用例
    	if(allCase==null) {
    		allCase=DataProviders.getInstance().dataP();   		
    	} 
    }

    //环境配置
    @BeforeClass
    public void envSetUp() {
        try {  //环境由filter配置
            RestAssured.baseURI = BASE_URI;
            //RestAssured.basePath = new GetFileMess().getValue("veryCodeUri", system);
            RestAssured.port = Integer.parseInt(PORT);
            //请求中有中文
            RestAssured.config=RestAssured.config()
                    .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"));
            //请求头设置
            RestAssured.filters((req,res,ctx)->{
                req.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36")
                        //.header("Content-Type","application/x-www-form-urlencoded")
                        .header("Cookie","jhop5-code=cbdd433ca1fe4d89abaa37438fdb472d")
                        .header("Accept","application/json, text/plain, */*")
                        .header("Accept-Encoding","gzip, deflate")
                        .header("Connection","keep-alive")
                        .header("Host",String.format("%s:%s",BASE_URI.replace("http://", ""),PORT))
                        .header("Referer",String.format("%s:%s/", BASE_URI,PORT));
                Response resOrigin = ctx.next(req,res);
//                //克隆原来的response   setBody修改响应内容，可用于加密响应解密处理
//                ResponseBuilder responseBuilder=new ResponseBuilder().clone(resOrigin);
//                //setbody是修改把返回来的body
//                responseBuilder.setBody("hello world");
//                //新的response
//                resOrigin =responseBuilder.build();
                return resOrigin;
                });
            token=RunCaseAPI.login(allCase);
			RestAssured.filters((req,res,ctx)->{
			
                req.header("Authorization",String.format("Bearer %s", token));
                Response resOrigin = ctx.next(req,res);
                return resOrigin;
            });
        } catch (Exception e) {
        	log.error("登录失败！");
            e.printStackTrace();
        }
    }

    /*
     *创建environment.properties并放到allure-results目录下，测试报告展现
     */
    @AfterSuite
    public void createEnvPropertiesForReport() throws IOException {
//        Map<String, String> data = new HashMap<>();
//        String database = "iiaccount_db.properties";
//        data.put("DatabaseLoginName", new GetFileMess().getValue("DB_Name", database));
//        data.put("DatabaseLoginPass", new GetFileMess().getValue("DB_Password", database));
//        data.put("DatabaseLoginIP", new GetFileMess().getValue("DB_IP", database));
//        data.put("baseURI", RestAssured.baseURI + ":" + RestAssured.port + "/" + RestAssured.basePath);

//        writePropertiesFile(data);
    }

    @AfterSuite
    public void dataTearDown() throws SQLException, IOException, ClassNotFoundException {
        //案例执行结束后，对数据池的数据进行清理（删除或更新状态）
 //       ExcutSqlFile.excute("tearDownSQL"); //案例执行后数据清理
    }
    
    
    
    

}
