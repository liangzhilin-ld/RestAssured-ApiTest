package com.techstar.datatransfer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.techstar.utils.GetFileMess;
import com.techstar.utils.ParameterHander;
import com.techstar.utils.TestLinkUtil;
import com.alibaba.fastjson.JSONObject;
import com.techstar.utils.Enum;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;

public class DataBuilders {
	private static Logger log = Logger.getLogger(DataBuilders.class);
	private static String config = "config.properties";
	private static String testLinkUrl;
	private static String testKey;
	private static String testProduct;
	private static volatile DataBuilders dbs;
	private static HashMap<String,TestSuite> allAPI;
	private DataBuilders() {}
	public static DataBuilders getInstance() {
        // Double-Check idiom
        if (dbs == null) {
            synchronized (TestLinkUtil.class) {
                // 只需在第一次创建实例时才同步
                if (dbs == null) {
                	dbs = new DataBuilders(); 
                }
            }
        }
        return dbs;
	}
	private void initTestLinkConnect() {
		GetFileMess pro=new GetFileMess();
		testLinkUrl=pro.getValue("testLinkUrl", config);
		testKey=pro.getValue("testKey", config);
		testProduct=pro.getValue("testProduct", config);
//		testPlan=pro.getValue("testPlan", config);
		TestLinkUtil.getInstance().setTestLinkUrl(testLinkUrl);
		TestLinkUtil.getInstance().setTestKey(testKey);
		TestLinkUtil.getInstance().setTestProduct(testProduct);
		TestLinkUtil.getInstance().InitializeTestLink();
	}
	/**
	 * 获取接口套件
	 * @return
	 */
	public HashMap<String,TestSuite> getApisforPlan(){
		if(allAPI==null) {
			initTestLinkConnect();		
			TestSuite[] suites=TestLinkUtil.getInstance().getSuites();
			allAPI=TestLinkUtil.getInstance().getAllApiUnderPlan(suites);
		}
		if(allAPI.isEmpty()) 
			log.error("TestLink中不存在符合条件的测试用例!");
		return allAPI;
	}
	/**
	 * 获取接口套件的header及req、response参数模板
	 * @param suiteName
	 * @return
	 */
	private HashMap<String,String> getSuiteDetail(String suiteName){
		HashMap<String,String> reqHashMap=new HashMap<String,String>();
		try {
			HashMap<String,TestSuite> map=getApisforPlan();		
			TestSuite suite=map.get(suiteName);	
			Document document = Jsoup.parse(suite.getDetails());
			Elements tr=document.select("tbody>tr");
			//testlink中套件详细表格有3行
			String headerString=tr.get(0).select("td").get(1).text();
			String inputString=tr.get(1).select("td").get(1).text();
			String outpuString=tr.get(2).select("td").get(1).text();
			reqHashMap.put(Enum.SuiteDatail.HEADER.getValue(),headerString);
			reqHashMap.put(Enum.SuiteDatail.INPUT.getValue(),inputString);
			reqHashMap.put(Enum.SuiteDatail.OUTPUT.getValue(),outpuString);
		} catch (Exception e) {
			e.getStackTrace();
			log.error("获取接口input/out参数模板出错,接口名称："+suiteName);
		}
		
		return reqHashMap;
	}
	
  
	/**
	 * 获取最终参数
	 * @param suiteName 套件名称
	 * @return
	 */
	public List<TestLinkCase> DataConverter(String suiteName) {
		HashMap<String,String> reqHashMap=getSuiteDetail(suiteName);
		//读取用例
		//List<HashMap<String,String>> listCases=new ArrayList<>();
		List<TestLinkCase> listCases=new ArrayList<>();
		TestCase[] tcCases=TestLinkUtil.getInstance().getTestCasesForSuite(suiteName);
	    for (TestCase testCase : tcCases) {
	    	TestLinkCase tc=TestLinkUtil.getInstance().getCaseDetail(testCase);
	    	//if(tc.invalid())continue;			
			String inputModle=reqHashMap.get(Enum.SuiteDatail.INPUT.getValue());
			String header=reqHashMap.get(Enum.SuiteDatail.HEADER.getValue());
			String outputModle=reqHashMap.get(Enum.SuiteDatail.OUTPUT.getValue());
			JSONObject jsonHeader = JSONObject.parseObject(header);
			String contentType = jsonHeader.getString(Enum.Header.CONTENTTYPE.getValue());
			
			if(contentType.toLowerCase().contains(Enum.ContentType.FORM.getValue())) {
				List<String> actionString=tc.action;
				tc.inputFormActual=ParameterHander.instance()
						.replaceFormResAction(inputModle,actionString);			
			}else {
				tc.inputJsonActual=ParameterHander.instance()
						.replaceJsonForAction(inputModle,tc.action);
			}
			tc.caseName=testCase.getName();
			tc.caseID=testCase.getId().toString();
			tc.header=jsonHeader;
			tc.outputModle=outputModle;
			listCases.add(tc);
//			System.out.println("前提条件："+tc.condition);
//			System.out.println("测试步骤："+tc.action);
//			System.out.println("期望结果："+tc.expected);
		}
	    if(listCases.size()==0)log.info(suiteName+"套件下无用例");
	    return listCases;
	}
}
