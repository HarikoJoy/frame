package com.frame.hariko.mybatis.plugin;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.messages.Messages;

public class BatchInsertPlugin extends MyBatisPlugin {
	private static String METHOD_NAME = "insertBatchSelective";

	public boolean validate(List<String> warnings) {
		return true;
	}

	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		addBatchInsertMethod(interfaze, introspectedTable);
		return true;
	}

	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		addBatchInsertSelectiveXml(document, introspectedTable);
		return true;
	}

	private void addBatchInsertMethod(Interface interfaze, IntrospectedTable introspectedTable) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
		importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));

		Method ibsmethod = new Method();

		ibsmethod.setVisibility(org.mybatis.generator.api.dom.java.JavaVisibility.PUBLIC);

		FullyQualifiedJavaType ibsreturnType = FullyQualifiedJavaType.getIntInstance();
		ibsmethod.setReturnType(ibsreturnType);

		ibsmethod.setName(METHOD_NAME);

		FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
		FullyQualifiedJavaType paramListType;
		if (introspectedTable.getRules().generateBaseRecordClass()) {
			paramListType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		} else {
			if (introspectedTable.getRules().generatePrimaryKeyClass()) {
				paramListType = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
			} else
				throw new RuntimeException(Messages.getString("RuntimeError.12"));
		}
		paramType.addTypeArgument(paramListType);

		ibsmethod.addParameter(new org.mybatis.generator.api.dom.java.Parameter(paramType, "records"));

		interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(ibsmethod);
	}

	public void addBatchInsertSelectiveXml(Document document, IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> columns = introspectedTable.getAllColumns();

		String incrementField = introspectedTable.getTableConfiguration().getProperties().getProperty("incrementField");
		if (incrementField != null) {
			incrementField = incrementField.toUpperCase();
		}

		XmlElement insertBatchElement = new XmlElement("insert");
		insertBatchElement.addAttribute(new Attribute("id", "insertBatchSelective"));
		insertBatchElement.addAttribute(new Attribute("parameterType", "java.util.List"));
		this.context.getCommentGenerator().addComment(insertBatchElement);

		XmlElement javaPropertyAndDbType = new XmlElement("trim");
		javaPropertyAndDbType.addAttribute(new Attribute("prefix", " ("));
		javaPropertyAndDbType.addAttribute(new Attribute("suffix", ")"));
		javaPropertyAndDbType.addAttribute(new Attribute("suffixOverrides", ","));

		XmlElement trim1Element = new XmlElement("trim");
		trim1Element.addAttribute(new Attribute("prefix", "("));
		trim1Element.addAttribute(new Attribute("suffix", ")"));
		trim1Element.addAttribute(new Attribute("suffixOverrides", ","));
		for (IntrospectedColumn introspectedColumn : columns) {
			String columnName = introspectedColumn.getActualColumnName();
			if (!columnName.toUpperCase().equals(incrementField)) {
				XmlElement iftest = new XmlElement("if");
				iftest.addAttribute(new Attribute("test", "list[0]." + introspectedColumn.getJavaProperty() + "!=null"));
				iftest.addElement(new TextElement(columnName + ","));
				trim1Element.addElement(iftest);
				XmlElement trimiftest = new XmlElement("if");
				trimiftest.addAttribute(new Attribute("test", "item." + introspectedColumn.getJavaProperty() + "!=null"));
				trimiftest.addElement(
						new TextElement("#{item." + introspectedColumn.getJavaProperty() + ",jdbcType=" + introspectedColumn.getJdbcTypeName() + "},"));
				javaPropertyAndDbType.addElement(trimiftest);
			}
		}

		XmlElement foreachElement = new XmlElement("foreach");
		foreachElement.addAttribute(new Attribute("collection", "list"));
		foreachElement.addAttribute(new Attribute("index", "index"));
		foreachElement.addAttribute(new Attribute("item", "item"));
		foreachElement.addAttribute(new Attribute("separator", ","));
		insertBatchElement.addElement(new TextElement("insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
		insertBatchElement.addElement(trim1Element);
		insertBatchElement.addElement(new TextElement(" values "));
		foreachElement.addElement(javaPropertyAndDbType);
		insertBatchElement.addElement(foreachElement);

		document.getRootElement().addElement(insertBatchElement);
	}
}
