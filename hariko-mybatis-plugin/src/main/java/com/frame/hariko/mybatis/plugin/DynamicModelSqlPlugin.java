package com.frame.hariko.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class DynamicModelSqlPlugin extends MyBatisPlugin {
	private boolean checkEmptyString = true;

	public boolean validate(List<String> warnings) {
		String prop = this.properties.getProperty("checkEmptyString");
		if ((prop != null) && ("false".equalsIgnoreCase(prop))) {
			this.checkEmptyString = false;
		}

		return true;
	}

	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		genSqlWhereXml(document, introspectedTable);

		return true;
	}

	private void genSqlWhereXml(Document document, IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("sql");
		Attribute attr = new Attribute("id", this.DYNAMIC_SQL_ID);
		element.addAttribute(attr);
		this.context.getCommentGenerator().addComment(element);
		int level1 = 1;
		int level2 = 2;
		int level3 = 3;
		int level4 = 4;
		StringBuilder sb = new StringBuilder();
		sb.append(indent(level1) + "<if test=\"cond != null\">\n");
		for (IntrospectedColumn col : introspectedTable.getAllColumns()) {
			String modelProperty = col.getJavaProperty();
			sb.append(indent(level3) + "<if test=\"cond.");
			sb.append(modelProperty);
			sb.append(" != null");
			if ((this.checkEmptyString) && ("String".equalsIgnoreCase(col.getFullyQualifiedJavaType().getShortName()))) {
				sb.append(" and cond.");
				sb.append(modelProperty).append(" != ''");
			}

			sb.append("\">\n");
			sb.append(indent(level4) + "and ");
			sb.append(col.getActualColumnName());
			if ("String".equalsIgnoreCase(col.getFullyQualifiedJavaType().getShortName())) {
				sb.append(" <if test =\"!strict\"> like concat('%', </if>");
				sb.append("<if test =\"strict\"> = </if>");
			} else {
				sb.append(" = ");
			}
			sb.append("#{cond." + modelProperty + ", jdbcType=" + col.getJdbcTypeName() + "}");
			if ("String".equalsIgnoreCase(col.getFullyQualifiedJavaType().getShortName())) {
				sb.append("<if test =\"!strict\">, '%') </if>");
			}
			sb.append("\n" + indent(level3) + "</if>\n");
		}
		sb.append(indent(level2) + "</if>");

		TextElement text = new TextElement(sb.toString());
		element.addElement(text);
		document.getRootElement().addElement(element);
	}
}
