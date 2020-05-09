package com.techstar.utils;

import java.net.URI;
import java.net.URISyntaxException;
import com.techstar.utils.GetFileMess;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


public class WebSocketClientUtil {
	private static String uri= "";
	private static WebSocketClient webSocketClient;
	private static String message = "";
	private static String conf = "config.properties";
	
	//设置uri
	public static void setUri(String url) {
		String ip= new GetFileMess().getValue("baseURI", conf);;
		String port= new GetFileMess().getValue("port", conf);;
		String uriString = "ws://" + ip + ":" + port + url;
		WebSocketClientUtil.uri = uriString;
	}
	
	//设置发送的消息
	public static void setMessage(String message) {
		WebSocketClientUtil.message = message;
	}
	
	//WebSocketClient连接
	public static void ClientConnect() {
		try {
			webSocketClient = new WebSocketClient(new URI(uri)) {
				//打开链接
				@Override 
				public void onOpen(ServerHandshake shake) { 
					System.out.println("握手。。。"); 
				} 
				//这个方法自动接收服务器发过来的信息,直接在此处调用自己写的方法即可.本人将消息存入到session中，别处可以监听，然后取出再清空
				@Override 
				public void onMessage(String msgString) {
					System.out.println("websocket返回消息" + msgString);
				}
				//客户端发生错误,即将关闭!
				@Override 
				public void onError(Exception e) { 
					System.out.println("发生错误已关闭"); 
				} 
				 
				//关闭链接
				@Override 
				public void onClose(int arg0, String arg1, boolean arg2) { 
					System.out.println("链接已关闭"); 
				}
			};
			try {
				webSocketClient.connectBlocking();
			} catch (InterruptedException e) {
				System.out.print("中断异常");
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	//关闭websocket连接
	public static void closeWebSocket() {
		webSocketClient.close();
	}
	
	public static void sendMessage() {
		webSocketClient.send(message);
	}
}
