package com.techstar.utils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHtml {
	private static Logger log = Logger.getLogger(ParseHtml.class);
	public String delHtmlTag(String str){ 
	    String newstr = ""; 
	    newstr = str.replaceAll("<[.[^>]]*>","");
	    newstr = newstr.replaceAll(" ", ""); 
	    newstr = StringEscapeUtils.unescapeHtml4(newstr);
	    return newstr;
	}
	public String stripHtml(String content) { 
		// <p>段落替换为换行 
		content = content.replaceAll("<p .*?>", ""); 
		content = content.replaceAll("<span .*?>", "");
		// <br><br/>替换为换行 
		content = content.replaceAll("<br\\s*/?>", "\r\n"); 
		// 去掉其它的<>之间的东西 
		content = content.replaceAll("\\<.*?>", ""); 
		// 去掉空格 
		content = content.replaceAll("&nbsp;", ""); 
		content = StringEscapeUtils.unescapeHtml4(content);
		// 还原HTML 
		// content = HTMLDecoder.decode(content); 
		return content; 
	}
	
	/**
	 * 
	 * @param content  解析内容
	 * @param cssPath  cssQuery表达式
	 * @return content
	 */
	public String getHtmlTagText(String content,String cssPath) { 
		String resultString=null;
		try {
			Document document = Jsoup.parse(content);
			Element ffDocument=document.selectFirst(cssPath);
			resultString=ffDocument.text();
		} catch (Exception e) {
			System.out.println("解析html内容异常："+e.getStackTrace());
			log.error("解析html内容异常："+e.getStackTrace());
		}		
		return resultString;
	}
	public List<String> getHtmlTagsText(String content,String cssPath) { 
		List<String> result=new ArrayList<String>() ;
		try {
			Document document = Jsoup.parse(content);
			Elements Documents=document.select(cssPath);
			if(Documents.isEmpty())
				Documents=document.select(cssPath.replace("p", "div"));
			for (Element element : Documents) {
				result.add(element.text().trim());
			}		
			if(result.size()==0) {
				return null;
			}
		} catch (Exception e) {
			System.out.println("解析html内容异常："+e.getStackTrace());
			log.error("解析html内容异常："+e.getStackTrace());
		}		
		return result;
	}
}
