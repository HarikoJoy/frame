package com.frame.hariko.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frame.hariko.entity.TextMessage;
import com.thoughtworks.xstream.XStream;

public class XmlUtil {
	/*
	 * xml转map

	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		HashMap<String, String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();

		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);

		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = (List<Element>)root.elements();

		for(Element e:list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}*/
	/*
	 * 文本消息对象转xml
	 */
	public static String textMsgToxml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
}
