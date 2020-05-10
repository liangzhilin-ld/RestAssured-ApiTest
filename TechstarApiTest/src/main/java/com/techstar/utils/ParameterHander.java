package com.techstar.utils;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.techstar.exceptions.ErrorException;
/**
* @author liangzhilin
* @date 2020年2月29日 下午6:43:42
* 类说明 请求字符串节点参数值替换
*/
public class ParameterHander {
	private static ParameterHander hander;
	Logger log = Logger.getLogger(ParameterHander.class);
	
	public static ParameterHander instance(){
		if (hander == null) {
	        synchronized (ParameterHander.class) {
	            // 只需在第一次创建实例时才同步
	            if (hander == null) {
	            	hander = new ParameterHander(); 
	            }
	        }
	    }
		return hander;
	}
	/**
	 * 根据用例Action步骤内容替换form类型请求模板中的对应字段值
	 * @param reqModle
	 * @param caseAction
	 * @return 实际请求Form字符串
	 */
	public HashMap<String, String> replaceFormResAction(String reqModle,List<String> caseAction) {
		HashMap<String, String> map=new HashMap<String, String>();
		if(StringUtil.isEmpty(reqModle)||caseAction.size()==0)
			return map;		
		try {
			String[] array=reqModle.split(" ");
			for (String str : array) {
				String keyString=str.split(":")[0].trim();
				String valueString=str.split(":")[1].trim();
				map.put(keyString, valueString);
			}
			for (String str : caseAction) {
				String keyString=str.split(":")[0].trim();
				String valueString=str.split(":")[1].trim();
				if(!map.containsKey(keyString))
					log.error(str+"替换失败，原始请求找不到key值:"+keyString);
				map.put(keyString, valueString);
				//reqModle= reqModle.replace(map.get(keyString), valueString);
			}
		} catch (Exception e) {
			log.error("Input模板为Form格式替换测试Action中值时出错:"+reqModle);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Json字符串节点值设置
	 * @param reqModle 请求参数模板
	 * @param caseAction Testlink中步骤如: $.step[1].type=sdsfsf
	 * @return 实际请求Json字符串
	 */
	public String replaceJsonForAction(String reqModle,List<String> caseAction) {
		if(StringUtil.isEmpty(reqModle)||caseAction.size()==0)
			return reqModle;
		JSONObject res=JSON.parseObject(reqModle);
		try {
			for (String action : caseAction) {
				String jsonPath=action.split("=")[0];//$.step[1].type
				String jsonPathValue=action.split("=")[1];
				boolean isSuccess=JsonUtil.setValueByJsonPath(res, jsonPath, jsonPathValue);
				if(!isSuccess)
					new ErrorException(this.getClass().getName()+"Json用例节点值替换处理失败");
			}
		} catch (Exception e) {
			log.error(this.getClass().getName()+"Json字符串用例值处理异常:\n"+reqModle);
			e.printStackTrace();
		}
		return res.toString();
		
	}
}
