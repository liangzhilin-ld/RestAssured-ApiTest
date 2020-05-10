package com.techstar.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
* @author liangzhilin
* @date 2020年2月29日 下午4:42:11
* 类说明 测试用例类模板
*/
public class TestLinkCase {

	public JSONObject header;
	public HashMap<String, String> inputFormActual;
	public String inputJsonActual;//实际发送参数
	public String outputModle;//期望响应内容结构
	public String caseID;
	public String caseName;
	public String condition;//前提条件
	public List<String> action=new ArrayList<String>();//需要修改的请求节点值
	public List<String> expected=new ArrayList<String>();//期望值
	public boolean invalid() {
		if(this.action.isEmpty())return true;
		return false;
	}
}
