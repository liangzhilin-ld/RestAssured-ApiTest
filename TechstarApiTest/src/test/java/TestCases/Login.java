package TestCases;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.techstar.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.techstar.asserts.RespondAssertForJson;
import com.techstar.basetest.SetUpTearDown;
import com.techstar.datatransfer.*;
public class Login extends SetUpTearDown{
	List<TestLinkCase> apiCases;
	Logger log = Logger.getLogger(Login.class);
	@BeforeClass
	public void beforeClass() {
		apiCases=allCase.get("context");
	}
	@Test
	public void testContextSuccess() {
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

//	@Test
	public void debug() {
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("str", "{\"processId\":\"null\"}");
		RestAssured.baseURI="http://172.16.206.64:8088";
		RestAssured.basePath="/vbi5/g";
				 
		Response rs=given()
        .contentType("application/x-www-form-urlencoded;charset=UTF-8")
        .request()
//        .log().all()
        .params(parameters)
        .post();
		log.info(rs.asString());
		String pid=JsonPath.from(rs.asString()).get("processId");
		JSONObject res=JSON.parseObject(rs.asString());
	}
}
