package TestCases;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.techstar.utils.HttpRequestUtil;
import com.techstar.utils.JsonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.techstar.asserts.RespondAssertForJson;
import com.techstar.basetest.SetUpTearDown;
import com.techstar.datatransfer.DataProviders;
import com.techstar.datatransfer.TestLinkCase;

import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

public class Login2 extends SetUpTearDown{
	
	List<TestLinkCase> apiCases;
	Logger log = Logger.getLogger(Login.class);
	@BeforeClass
	public void beforeClass() {
		apiCases=allCase.get("context");
	}
	@Test
	public void testContextSuccess2() {
		String caseName="获取登录账号信息";
		Reporter.log(caseName);
		TestLinkCase testCa = DataProviders.getInstance().getTestCase(apiCases, caseName);
		JSONObject input=JSON.parseObject(testCa.inputJsonActual);
		JsonUtil.setValue(input,"$.username", VBI_NAME);
		testCa.inputJsonActual=input.toString();
		
		testCa.expected.set(0, testCa.expected.get(0).replace("admin", VBI_NAME));
		RespondAssertForJson.assertResult(testCa);
		
		//log.info(response.asString());
		//String reString=JsonPath.from(response.asString()).get("data");
		//String veryString=AESUtil.decrypt(reString, "6e6757620d3fcbfc");
		//response.then().body(arguments, responseAwareMatcher);
		//log.info("响应内容:"+reString);
		
	}

 

}
