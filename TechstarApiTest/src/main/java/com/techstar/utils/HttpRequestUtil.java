package com.techstar.utils;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class HttpRequestUtil {
	private static Logger log = Logger.getLogger(HttpRequestUtil.class);
    /*
     *post或get方式请求,返回响应报文（json格式）
     *@bodyString:json格式的请求报文体
     *@para:requestType post或get
     */
    public static Response runCase(String bodyString,String requestType,String uri){
        Response response = null;
        if(requestType.toLowerCase().equals("get"))
            response = given()
                    .contentType("application/json;charset=UTF-8")
                    .request()
                    .body(bodyString)
                    .get(uri);
        else
            response = given()
                    .contentType("application/json;charset=UTF-8")
                    .request()
                    .body(bodyString)
                    .post(uri);

        //打印格式化的参数
        //response.prettyPrint(); ////去掉部分日志 add by lrb 20181029
        if(response.getStatusCode()!=200)log.error(response.prettyPrint());
        return response;
    }
	/**
	 * 表单格式请求
	 * @param map 表单请求方式map参数
	 * @param requestType post或get
	 * @return
	 */
    public static Response runCase(HashMap<String, String> map,String requestType,String uri){
        Response response = null;
        if(requestType.toLowerCase().equals("get"))
            response = given()
                    .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                    .request()
                    .params(map)
                    .when()
                    .get(uri);
        else
            response = given()
                    .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                    .request()
                    .params(map)
                    .when()
                    .post(uri);
        if(response.getStatusCode()!=200)log.error(response.prettyPrint());
        return response;
    }
}
