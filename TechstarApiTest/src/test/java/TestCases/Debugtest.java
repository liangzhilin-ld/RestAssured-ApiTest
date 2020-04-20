package TestCases;
import static io.restassured.RestAssured.given;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONObject;
import com.techstar.utils.GetFileMess;
import com.techstar.utils.sm.SMUtils;

import io.restassured.response.Response;
public class Debugtest {
	Logger log = Logger.getLogger(Debugtest.class);
  //@Test
  public void test() throws Exception {
	  String dbString=new GetFileMess().getValue("baseDB", "jdbc.properties");
	  String keypairString="e9664b2bebccb6fe80da086044608115";
	  //带多个值得查询参数
	  //json参数用.body(jsonstr)
	  HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("str", "{\"processId\":\"null\"}");
		//parameters.put("id", "14");


	  Response rs=given()
		        .contentType("application/x-www-form-urlencoded;charset=UTF-8")
		        .request()
		        .when()
		        .log().all()
		        .get("http://172.16.206.128:8080/auth/g");
	  Response image=given()
				.request()
				.when()
				.get("http://172.16.206.128:8080/auth/code/image");
	  //Object resultJson = JSONObject.parse(rs.asString());
	  //log.info("G接口"+JSONObject.parseObject(resultJson.toString()));
	  String gdata=SMUtils.sm4_ecb_decrypt(keypairString,rs.jsonPath().get("data"));
	  String publicKey=JSONObject.parseObject(gdata).getString("publicKey");
	  String token=JSONObject.parseObject(gdata).getString("id");
	  String veryCode=SMUtils.sm4_ecb_decrypt(keypairString,image.jsonPath().get("data"));
	  
	  String name=SMUtils.sm2Encrypt(publicKey,"admin");
	  String pwd=SMUtils.sm2Encrypt(publicKey,"Htsd@2020");
	  
	  System.out.println(name);
	  System.out.println(pwd);
	  String vercode=veryCode.split(",")[1];
	  String deviceid=veryCode.split(",")[0];
	  
	  String jsonString=String.format("{\"username\":\"%s\",\"password\":\"%s\",\"icode\":\"%s\",\"deviceId\":\"%s\",\"id\":\"%s\"}",name,pwd,vercode,deviceid,token);
	 
	  
	  Response pw=given()
			    .header("content-type","application/json")
				.request()
				.log().all()
				.body(jsonString)
				.when()
				.post("http://172.16.206.128:8080/auth/login/pw");
	  log.info(pw.asString());
	  
  }

  @Test
  public void test2() throws Exception {
	  String publicKey="04F3D11CCF22121FC7F413F50446DF06F30656A34E5FF38D4D568526CB04E4D6F4930396B340DABC0677F2E683320E3EC52E99E009A08C84428F64A49BEE3A316C";
	  String userName="04CD87FEFC22B0118B81DB3B26261586B8A0598EAE90936DA201078B568578CDA6F5B2066EF671DBF46B0DD6CF44E08D1A5E342A808689F230A615384F497C1A895415CB3F19546E64B70C59562FB5C1EA176D9C563CA5CF12A05E4301943922240E18925EAEC9A030424A678E6DAD54AFD56FCD219AF3A02B91537E9EF9EC2B7F001101A32367AFD8F9572D0B29E5EC356F59C3B39EDB065C7BF14962DABC5E9C09DE12CAED2386D2EE6908ED3790BB67044EB7748423469946EE1F03368ED00AA6A8CA9E580EA33A0B9B629ED381B5F234F09E41CB77A038A3FECC5960E4094E5D8E4318B0F0BEF014620FF6CDA4C1F1A2439C021709AA5EF22ED0EDD5B7BF4C3895CA0D3CDEB3DD074947BCDE5483DFBD382107DA0D91417748B2D8B90B2D2FABD41E4467B343E40722CC7D";
	  String pwd="04525784DC35A458F4E8957C226A56DD9A73E3CDA37129625C3BC6BCC1E8FC0443C5FDCA3E4AB1DD11C02DFB2667F86791C3BBCB4B2E5D1F7A59EE59BFB76364159639D9D34087B97C42659466D34C855F95A2E832B0412DF5736745DC59725C37974ED8AA0FF75E4724B7F9B7B9A34A86F461365C1696DBC67E4464C1DA732A9670444193C78D1FFEEA808777D67605A7C9E1BE2C7EF2D5854190AF43C8B881E3E1EC623C629E7748493E33039E1F8F4BDD141466D3C9F30DB1A4169E936BCE0E5536B3451B57D7173ECD4B5B29891E8402381113CF8D2163EB0CB4AF7245C8D4538652A89A7AE8AF5B2AD87E97CB469B31AD929BD71E6A8210CB8D468829A44BE22D37540DA7CD0DBC54D04B2F2722513B4EEC7140E2C08EE6F6A6B46AEDCE0AC847B686A7BDE0F8565CE77C7AA997BF81F5F3A11227C6E30BF27BD1";
	  log.info(SMUtils.sm2decrypt(publicKey, userName));
	  log.info(SMUtils.sm2decrypt(publicKey, userName));
  }

}
