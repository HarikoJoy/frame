package com.frame.hariko.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.frame.hariko.entity.TextMessage;
import com.frame.hariko.wx.News;
import com.frame.hariko.wx.NewsMessage;
import com.thoughtworks.xstream.XStream;


public class MessageUtil {
	public static final String MSGTYPE_EVENT = "event";//消息类型--事件
	public static final String MESSAGE_SUBSCIBE = "subscribe";//消息事件类型--订阅事件
	public static final String MESSAGE_UNSUBSCIBE = "unsubscribe";//消息事件类型--取消订阅事件
	public static final String MESSAGE_TEXT = "text";//消息类型--文本消息

	/*
	 * 组装文本消息
	 */
	public static String textMsg(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return XmlUtil.textMsgToxml(text);
	}

	/*
	 * 响应订阅事件--回复文本消息
	 */
	public static String subscribeForText(String toUserName,String fromUserName){
		return textMsg(toUserName, fromUserName, "欢迎关注，精彩内容不容错过！！！");
	}

	/*
	 * 响应取消订阅事件
	 */
	public static String unsubscribe(String toUserName,String fromUserName){
		//TODO 可以进行取关后的其他后续业务处理
		System.out.println("用户："+ fromUserName +"取消关注~");
		return "";
	}

	public static String initNewsMessage(String toUSerName, String fromUserName) {
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		//组建一条图文↓ ↓ ↓
		News newsItem = new News();
		newsItem.setTitle("欢迎关注我的公众号");
		newsItem.setDescription("进行操作之前请先注册！");
		//newsItem.setPicUrl(WXConstants.BASE_SERVER + "/image/wx/login_article_cover.png");
		newsItem.setPicUrl("http://pic3.nipic.com/20090702/918855_174429094_2.jpg");
		newsItem.setUrl("http://www.baidu.com");
		newsList.add(newsItem);
		//组装图文消息相关信息
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUSerName);
		newsMessage.setCreateTime(new Date().getTime() + "");
		newsMessage.setMsgType("news");
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		//调用newsMessageToXml将图文消息转化为XML结构并返回
		return MessageUtil.newsMessageToXml(newsMessage);
	}
	/**
	 * 图文消息转XML结构方法
	 */
	public static String newsMessageToXml(NewsMessage message) {
		XStream xs = new XStream();
		//由于转换后xml根节点默认为class类，需转化为<xml>
		xs.alias("xml", message.getClass());
		xs.alias("item", new News().getClass());
		return xs.toXML(message);
	}
}
