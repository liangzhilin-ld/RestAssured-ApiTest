package com.techstar.utils;
/**
 * 枚举类型常量定义
 * @author techstar
 *
 */
public class Enum{
	/**
	 * TestLink最底层套件接口详细，三个key字段常量
	 * @author techstar
	 *
	 */
	public static enum SuiteDatail {
        HEADER("Header","套件详细header"),
        INPUT("Input","套件详细input"),
        OUTPUT("Output","套件详细output");
		private SuiteDatail(String value,String name){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;
        
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
	}
	/**
	 * 请求头常量
	 * @author techstar
	 *
	 */
	public static enum Header {
        PROTOCOL("Protocol","请求协议"),
        METHOD("Method","请求方法"),
        CONTENTTYPE("Content-Type","请求内容type"),
		URI("Uri","请求内容type");
		private Header(String value,String name){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;
        
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
	}
	/**
	 * 协议类型常量定义
	 * @author techstar
	 *
	 */
	public static enum Protocol {
		HTTP("http","http请求"),
		WS("ws","websocket请求");
		private Protocol(String value,String name){
            this.value=value;
            this.name=name;
        }
	    private final String value;
	    private final String name;
	        
	    public String getValue() {
	    	return value;
	    }
	    public String getName() {
	        return name;
	    }
	}
	/**
	 * 请求内容格式常量定义
	 * @author techstar
	 *
	 */
	public static enum ContentType {
        JSON("json","JSON格式请求"),
        FORM("form","表单格式请求");
		private ContentType(String value,String name){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;
        
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
	}
	/**
	 * 请求方法常量
	 * @author techstar
	 *
	 */
	public static enum Method {
		POST("POST","POS请求T"),
		GET("GET","GET请求");
		private Method(String value,String name){
            this.value=value;
            this.name=name;
        }
	       private final String value;
	        private final String name;
	        
	        public String getValue() {
	            return value;
	        }
	        public String getName() {
	            return name;
	        }
	}
	/**
	 * 封装TestLink用例中key值定义
	 * @author techstar
	 *
	 */
	public static enum CaseField {
		CASEID("caseID","用例ID"),
		CASENAME("casename","用例名称"),
		CONDITION("condition","前提条件"),
		ACTION("action","测试步骤"),
		EXPECTED("expected","预期结果");
		private CaseField(String value,String name){
            this.value=value;
            this.name=name;
        }
	    private final String value;
	    private final String name;
	        
	    public String getValue() {
	    	return value;
	    }
	    public String getName() {
	        return name;
	    }
	}

}

