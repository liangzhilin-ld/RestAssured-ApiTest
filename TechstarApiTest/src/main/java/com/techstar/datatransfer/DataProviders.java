package com.techstar.datatransfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.techstar.exceptions.ErrorException;
import com.techstar.utils.TestLinkUtil;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;

public class DataProviders {
	private static Logger log = Logger.getLogger(DataProviders.class);
	private static volatile DataProviders tetsCases;
	private DataProviders() {

	}
	public static DataProviders getInstance() {
        // Double-Check idiom
        if (tetsCases == null) {
            synchronized (TestLinkUtil.class) {
                // 只需在第一次创建实例时才同步
                if (tetsCases == null) {
                	tetsCases = new DataProviders(); 
                }
            }
        }
        return tetsCases;
	}
	public HashMap<String,List<TestLinkCase>> dataP() {
		HashMap<String,List<TestLinkCase>> tscHashMap=new HashMap<String,List<TestLinkCase>>();
		HashMap<String,TestSuite> sutiesHashMap=DataBuilders.getInstance().getApisforPlan();
		for (Entry<String, TestSuite> entry : sutiesHashMap.entrySet()) {
			TestSuite tSuite=entry.getValue();
			List<TestLinkCase> tsCases=DataBuilders.getInstance().DataConverter(tSuite.getName());
			if(tsCases.size()==0)continue;
			tscHashMap.put(tSuite.getName(), tsCases);
		}
		return tscHashMap;
	}
	public TestLinkCase getTestCase(List<TestLinkCase> ltc,String name) {
		TestLinkCase testCa=null;
		try {
			for (TestLinkCase tc : ltc) {
	            if(tc.caseName.equals(name)){
	            	testCa=tc;
	                break;
	            }
	        }
			if(testCa==null)throw new ErrorException(name+"用例获取失败");
		} catch (Exception e) {
			log.error("MethodName"+e.getStackTrace()[1].getMethodName());
			e.printStackTrace();
		}		
		return testCa;
	}
}
 