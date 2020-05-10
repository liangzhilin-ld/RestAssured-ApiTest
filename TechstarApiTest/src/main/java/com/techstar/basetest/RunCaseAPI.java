package com.techstar.basetest;
import java.util.HashMap;
import java.util.List;
import org.testng.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.techstar.datatransfer.DataProviders;
import com.techstar.datatransfer.TestLinkCase;
import com.techstar.utils.Enum;
import com.techstar.utils.HttpRequestUtil;
import com.techstar.utils.JsonUtil;
import com.techstar.utils.sm.SMUtils;

import io.restassured.response.Response;
/**
* @author liangzhilin
* @date 2020年3月13日 下午12:15:37
* 类说明
*/
public class RunCaseAPI {
	public static String smKey=Constants.SM_KEY;
	public static String vbLoginName=Constants.VBI_NAME;
	public static String vbLoginPwd=Constants.VBI_PWD;
	
	public static Response runCase(TestLinkCase testcase) {
		Response response = null;
		String method=testcase.header.getString(Enum.Header.METHOD.getValue());
		String contentType=testcase.header.getString(Enum.Header.CONTENTTYPE.getValue());
		String pro=testcase.header.getString(Enum.Header.PROTOCOL.getValue());
		String uri=testcase.header.getString(Enum.Header.URI.getValue());
		//RestAssured.baseURI=uri;
		if(pro.toLowerCase().contains("http")) {
			if(contentType.toLowerCase().contains("form")){
				response=HttpRequestUtil.runCase(testcase.inputFormActual,method,uri);
			}
			else {
				response=HttpRequestUtil.runCase(testcase.inputJsonActual,method,uri);
			}
		}else {
			//websocket请求
		}
		return response;		
	}

	public static String login(HashMap<String,List<TestLinkCase>> testlink) throws Exception{
		//获取秘钥publicKey与token
		TestLinkCase testCa = DataProviders.getInstance().getTestCase(testlink.get("g"), "g接口获取加密公钥");
		Response response=RunCaseAPI.runCase(testCa);
		String gdata=SMUtils.sm4_ecb_decrypt(smKey,response.jsonPath().get("data"));
		String publicKey=JSONObject.parseObject(gdata).getString("publicKey");
		String id=JSONObject.parseObject(gdata).getString("id");
		//获取验证码vercode与设备id
		testCa=DataProviders.getInstance().getTestCase(testlink.get("image"), "获取验证码接口");
		response=RunCaseAPI.runCase(testCa);
		String imagData=SMUtils.sm4_ecb_decrypt(smKey,response.jsonPath().get("data"));
		String vercode=imagData.split(",")[1];
		String deviceid=imagData.split(",")[0];
		//账号加密处理病登录
		String name=SMUtils.sm2Encrypt(publicKey,vbLoginName);
		String pwd=SMUtils.sm2Encrypt(publicKey,vbLoginPwd);
		testCa=DataProviders.getInstance().getTestCase(testlink.get("pw"), "正常登录验证");
		JSONObject input=JSON.parseObject(testCa.inputJsonActual);
		JsonUtil.setValueByJsonPath(input, "$.username", name);
		JsonUtil.setValueByJsonPath(input, "$.password", pwd);
		JsonUtil.setValueByJsonPath(input, "$.icode", vercode);
		JsonUtil.setValueByJsonPath(input, "$.deviceId", deviceid);
		JsonUtil.setValueByJsonPath(input, "$.id", id);
		testCa.inputJsonActual=input.toString();
		response=RunCaseAPI.runCase(testCa);
		String token=JsonUtil.getStringValue(
				JSON.parseObject(response.asString()), "$.data.access_token");		
		Assert.assertTrue(response.asString().contains("认证成功"), "登录失败");
		return token;
	}
}
