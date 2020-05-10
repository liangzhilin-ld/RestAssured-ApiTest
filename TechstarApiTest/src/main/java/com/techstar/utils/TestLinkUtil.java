package com.techstar.utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.net.MalformedURLException;
import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import org.apache.log4j.Logger;

import com.techstar.datatransfer.TestLinkCase;


/**
 * 参考https://blog.csdn.net/u011541946/article/details/80551082
 */

public class TestLinkUtil {
	private static Logger log = Logger.getLogger(TestLinkUtil.class);
	private String testProduct;
	private String testLinkUrl;
	private String testKey;
	private static TestLinkAPI api;
	private TestSuite[] suites;
	private static volatile TestLinkUtil testlink;//使用volatile关键字防止重排序
	public String getTestProduct() {
		return testProduct;
	}
	public void setTestProduct(String testProduct) {
		this.testProduct = testProduct;
	}
	public String getTestLinkUrl() {
		return testLinkUrl;
	}
	public void setTestLinkUrl(String testLinkUrl) {
		this.testLinkUrl = testLinkUrl;
	}
	public String getTestKey() {
		return testKey;
	}
	public void setTestKey(String testKey) {
		this.testKey = testKey;
	}
	public TestSuite[] getSuites() {
		return suites;
	}
	private TestLinkUtil() {

	}
	// 使用静态内部类创建外部类对象
	public static TestLinkUtil getInstance() {
        // Double-Check idiom
        if (testlink == null) {
            synchronized (TestLinkUtil.class) {
                // 只需在第一次创建实例时才同步
                if (testlink == null) {
                	testlink = new TestLinkUtil(); 
                }
            }
        }
        return testlink;
	}
	/**
	 * 初始化连接
	 */
	public void InitializeTestLink(){	
		api=connect(); 
		suites=getFirstLeverTestSuite(testProduct);
	}
	/**
	 * 连接testlink方法
	 * @return， 返回testlink连接对象api
	 */
	private TestLinkAPI connect() {
		String url = testLinkUrl;
		String devKey =testKey;
	    TestLinkAPI api = null;	     
	    URL testlinkURL = null;
	     
	    try {
	            testlinkURL = new URL(url);
	    } catch ( MalformedURLException mue )   {
	            mue.printStackTrace( System.err );
	            System.exit(-1);
	    }
	     
	    try {
	    	
	           api = new TestLinkAPI(testlinkURL, devKey);
	           
	    } catch( Exception te) {
	            te.printStackTrace( System.err );
	            System.exit(-1);
	    }
	     
	    System.out.println(api.ping());
		return api;
	}
	/**
	 * 根据项目名称字符串得到项目id，方便下一个方法（得到项目测试计划）调用
	 * @param api，连接对象，几乎这个类大部分方法都需要这个对象
	 * @param projectName
	 * @return， 返回项目ID号
	 */
	public int getProjectIdByName(String projectName) {
		
		//根据项目名称得到这个项目
		int projectId=0;
		try {
			TestProject project = api.getTestProjectByName(projectName);
			projectId = project.getId();
		} catch (Exception e) {
			log.error("获取产品ID异常:"+e.getStackTrace());			
		}
		return projectId;
	}
	
	/**
	 * 根据项目id得到项目下所有的测试计划
	 * @param api
	 * @param ProjectId
	 * @return，测试计划数组对象
	 */
	public TestPlan[] getAllPlanUnderProject(int ProjectId) {
		
		TestPlan[] plans = api.getProjectTestPlans(ProjectId);
		return plans;
	}
	
	/**
	 * 根据项目名称获取项目下顶层的测试套件数组对象
	 * @param api
	 * @param projectName
	 * @return 测试套件数组
	 */
	public TestSuite[] getFirstLeverTestSuite(String projectName) {
		try {
			int projectId = getProjectIdByName(projectName);
			TestSuite[] suites = api.getFirstLevelTestSuitesForTestProject(projectId);	
			return suites;
		} catch (Exception e) {
			log.error("TestLink初始化获取顶层套件异常");
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * 根据测试计划获取测试套件
	 * @param planName
	 * @return
	 */
	public TestSuite[] getTestSuitesForPlan(String planName) {

		TestPlan plan=api.getTestPlanByName(planName, testProduct);
		TestSuite[] suitesForPlan=api.getTestSuitesForTestPlan(plan.getId());
		return suitesForPlan;
	}
	/**
	 * 根据顶层套件组，按套件名称查询套件
	 * @param suites  测试计划顶层套件
	 * @param suiteName 接口名称(套件名称)
	 * @return 单个套件
	 */
	public TestSuite getTestSuiteByName(TestSuite[] suites, String suiteName) {
		TestSuite suite=null;		
		for (TestSuite testSuite : suites) {
			if(testSuite.getName().equals(suiteName)) {
				suite=testSuite;
				break;
			}else {
				TestSuite[] suites_sun = api.getTestSuitesForTestSuite(testSuite.getId());
				if(suites_sun.length>0) {
					TestSuite suite2=getTestSuiteByName(suites_sun,suiteName);
					if(suite2!=null&&suite2.getName().equals(suiteName)) {
						suite=suite2;
						break;
					}
				}
			}
		
		}
		return suite;
	}
	
	/**
	 * 根据项目名称获取项目下所有测试用例
	 * @param api
	 * @param projectName
	 * @return 用例列表
	 */
	public List<TestCase> getAllCasesUnderProject(String projectName) {
		
		TestSuite[] suites = getFirstLeverTestSuite(projectName);
		List<TestCase> cases=new ArrayList<TestCase>();
		//遍历数组，拿到每个测试套件下测试用例
		for (TestSuite testSuite : suites) {
			//获取测试用例数组,注意参数写法
			TestCase[] casesarry = api.getTestCasesForTestSuite(testSuite.getId(), true, null);
			if(casesarry.length>0) {
				for (TestCase testcase : casesarry) {
					cases.add(testcase);
				}
			}
		}
		return cases;
	}
	/**
	 * 获取测试计划下所有接口套件
	 * @param suites
	 * @return准确性待验证
	 */
	public HashMap<String,TestSuite> getAllApiUnderPlan(TestSuite[] suites) {
		HashMap<String,TestSuite> mapsuite=new HashMap<String,TestSuite>();
		for (TestSuite testSuite : suites) {
			TestSuite[] suites_sun = api.getTestSuitesForTestSuite(testSuite.getId());			
			if(suites_sun.length>0) {
				HashMap<String,TestSuite> suite2=getAllApiUnderPlan(suites_sun);
				if(suite2!=null) {
					mapsuite.putAll(suite2);
					suite2=null;
				}				
			}else {
				if(testSuite.getDetails()!=null)//套件详细为空的排出
					mapsuite.put(testSuite.getName(),testSuite);
			}
			suites_sun=null;
		}
		return mapsuite;
	}
	/**
	 * 根据项目名称获取项目下，顶层的测试套件顶层的测试用例数组对象
	 * @param api
	 * @param projectName
	 * @suiteName套件名称，对应接口名称
	 * @return 单个测试套件下用例数组
	 */
//	public static TestCase[] getTestCasesForSuite(TestLinkAPI api, String projectName) {
//		
//		TestSuite[] suites = TestLinkUtil.getFirstLeverTestSuite(api, projectName);
//		TestCase[] cases = null;
//		//遍历数组，拿到每个测试套件下测试用例
//		for (TestSuite testSuite : suites) {
//			//获取测试用例数组,注意参数写法
//			cases = api.getTestCasesForTestSuite(testSuite.getId(), true, null);
//		}
//		return cases;
//	}
	public TestCase[] getTestCasesForSuite(String suiteName) {
		
		//TestSuite[] suites = TestLinkUtil.getFirstLeverTestSuite(projectName);
		TestCase[] cases = null;
		//遍历数组，拿到每个测试套件下测试用例
		TestSuite testSuite=getTestSuiteByName(suites,suiteName);
		if(testSuite==null)testSuite=suites[0];
		cases = api.getTestCasesForTestSuite(testSuite.getId(), true, null);
		return cases;
	}


	/**
	 * 根据测试用例对象，得到测试步骤中的StepAction
	 * @param tCase
	 * @return
	 */
	public String getTestCaseAction(TestCase tCase) {
		//TestLinkAPI api,
		TestCase tc = api.getTestCaseByExternalId(tCase.getFullExternalId(), null);
		List<TestCaseStep> tcs = tc.getSteps();
		
		String action = null;
		//遍历steps集合
		for (TestCaseStep testCaseStep : tcs) {
			
			//开始数据清洗
			action = testCaseStep.getActions();
//			action=delHtmlTag(originalStr);
//			//先切除<p>和</p>标记
//			String s1 = originalStr.split("<p>")[1].split("</p>")[0];
//			//System.out.println(s1);  //debug用打印
//			//然后把<br />全部替换为空
//			String s2 = s1.replaceAll("<br />", "");
//			//System.out.println(s2);  //debug用打印
//			//把&quto改成正常显示双引号
//			action = StringEscapeUtils.unescapeHtml4(s2);
//			//System.out.println(action);
		}
		
		return action;
	}
	/**
	 * 根据测试用例对象，得到测试步骤中的ExpectedResult
	 * @param tCase
	 * @return
	 */
	public String getExpectedResult(TestCase tCase) {
			
			TestCase tc = api.getTestCaseByExternalId(tCase.getFullExternalId(), null);
			List<TestCaseStep> tcs = tc.getSteps();
			
			String result = null;
			//遍历steps集合
			for (TestCaseStep testCaseStep : tcs) {
				
				result = testCaseStep.getExpectedResults();
//				//System.out.println(expectResult);
//				
//				//数据清洗
//				//去除最外层的<p> </p>		
//				String s1 = expectResult.split("<p>")[1].split("</p>")[0];
//				//把全部br换行符替换成空字符
//				String s2 = s1.replaceAll("<br />", "");
//				//为了双引号在控制台正常显示
//				result = StringEscapeUtils.unescapeHtml4(s2);
			}
			
			return result;
			
	}
	/**
	 * 根据测试用例对象，得到测试前提getPreconditions
	 * @param tCase
	 * @return
	 */
	public String getPreconditions(TestCase tCase) {
			
			TestCase tc = api.getTestCaseByExternalId(tCase.getFullExternalId(), null);
			String condition = tc.getPreconditions();
			return condition;
			
	}
	/**
	 * 获取测试前提、步骤、期望内容，根据用例组织方式只需获取第1条记录
	 * @param tCase
	 * @return 返回用例详细
	 */
	public TestLinkCase getCaseDetail(TestCase tCase){
		TestLinkCase tlc=new TestLinkCase();
		ParseHtml getText=new ParseHtml();
		//get(0)获取首条步骤Action与期望ExpectedResult数据
		try {
			TestCase tc = api.getTestCaseByExternalId(tCase.getFullExternalId(), null);		
			List<TestCaseStep> tcs = tc.getSteps();
			if(tcs.size()==0)return tlc;
			String condition = tc.getPreconditions();		
			//expectResult去除“$.”rest-assured的json路径格式无$.符号
			String expectResult=tcs.get(0).getExpectedResults().replace("$.", "");
			String actionStr = tcs.get(0).getActions();		
			condition=getText.getHtmlTagText(condition,"body");
			List<String> action=getText.getHtmlTagsText(actionStr, "body>p");
			List<String> expected=getText.getHtmlTagsText(expectResult, "body>p");
			tlc.condition=condition;
			if(action!=null)
				tlc.action.addAll(action);
			tlc.expected.addAll(expected);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用例详情获取异常:"+e.getStackTrace());
		}
		
		return tlc;
	}
}
