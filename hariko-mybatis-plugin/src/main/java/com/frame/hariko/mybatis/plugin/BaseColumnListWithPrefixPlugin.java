package com.frame.hariko.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class BaseColumnListWithPrefixPlugin extends MyBatisPlugin {
	private static String SQL_ID = "Base_Column_List_Prefix";
	private static String PREFIX = "my.";

	public boolean validate(List<String> warnings) {
		return true;
	}

	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("sql");
		Attribute attr = new Attribute("id", SQL_ID);
		element.addAttribute(attr);
		this.context.getCommentGenerator().addComment(element);
		StringBuilder sb = new StringBuilder();
		for (IntrospectedColumn col : introspectedTable.getAllColumns()) {
			sb.append(PREFIX).append(col.getActualColumnName()).append(",");
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}

		TextElement text = new TextElement(sb.toString());
		element.addElement(text);
		document.getRootElement().addElement(element);

		return true;
	}
}
