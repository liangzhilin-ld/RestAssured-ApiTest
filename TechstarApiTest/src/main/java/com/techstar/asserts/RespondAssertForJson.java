package com.techstar.asserts;

import java.util.List;

import org.testng.Assert;

import com.techstar.basetest.RunCaseAPI;
import com.techstar.datatransfer.TestLinkCase;

import io.restassured.response.Response;

public class RespondAssertForJson {

	/**
	 * 针对预期结果直接从TestLinkCase的expected获取的情况
	 * @param tc
	 */
	public static void assertResult(TestLinkCase tc) {
		Response response=RunCaseAPI.runCase(tc);
		List<String> expecList=tc.expected;	
		for (String expl : expecList) {
			String[] exceped=expl.split("=");
			String reString=response.jsonPath().getString(exceped[0].trim());
			Assert.assertTrue(reString.equals(exceped[1].trim()),
					String.format("用例[%s]校验不通过！预期结果:", tc.caseName,expl));
		}
		
	}
}
