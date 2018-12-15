package com.frame.hariko.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class SelectOneByModelSelectivePlugin extends MyBatisPlugin {
	private final String methodName = "selectOneByModelSelective";

	public boolean validate(java.util.List<String> warnings) {
		return true;
	}

	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		genMethod(interfaze, topLevelClass, introspectedTable);
		return true;
	}

	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		genMapperSql(document, introspectedTable);
		return true;
	}

	private void genMethod(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();
		getClass();
		Method method = new Method(methodName);
		method.setVisibility(org.mybatis.generator.api.dom.java.JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType(modelName));
		org.mybatis.generator.api.dom.java.Parameter parameter = new org.mybatis.generator.api.dom.java.Parameter(new FullyQualifiedJavaType(modelName), "cond",
				"@Param(\"cond\")");
		method.addParameter(parameter);
		parameter = new org.mybatis.generator.api.dom.java.Parameter(new FullyQualifiedJavaType("java.lang.boolean"), "strict", "@Param(\"strict\")");
		method.addParameter(parameter);

		genMethodComment(method, introspectedTable);
		interfaze.addMethod(method);
	}

	private void genMethodComment(Method method, IntrospectedTable introspectedTable) {
		method.addJavaDocLine("/**");
		method.addJavaDocLine(" * 此方法返回满足条件的单条记录.<br/>");
		method.addJavaDocLine(" * 此返回条件判断其值不为 null且不为空'';<br/>");
		method.addJavaDocLine(" * 若strict为true则精确匹配所有值,若为false则模糊匹配所有类型为String的值;<br/>");
		method.addJavaDocLine(" * 注意：java的Date不能完成匹配Oracle Date类型，即insert date into table 然后使用同一值date select是取不到对应记录的.<br/>");
		method.addJavaDocLine(" * ");
		method.addJavaDocLine(" * This method was generated by MyBatis Generator.<br/>");

		StringBuilder sb = new StringBuilder();
		sb.append(" * This method corresponds to the database table ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		method.addJavaDocLine(sb.toString());

		addJavadocTag(method, false);

		method.addJavaDocLine(" */");
	}

	@SuppressWarnings("unused")
	private void genMapperSql(Document document, IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("select");
		getClass();
		Attribute attribute = new Attribute("id", "selectOneByModelSelective");
		element.addAttribute(attribute);
		attribute = new Attribute("parameterType", "map");
		element.addAttribute(attribute);
		attribute = new Attribute("resultMap", "BaseResultMap");
		element.addAttribute(attribute);
		this.context.getCommentGenerator().addComment(element);

		StringBuilder sb = new StringBuilder();
		int level1 = 1;
		int level2 = 2;
		int level3 = 3;
		int level4 = 4;
		int level5 = 5;

		sb.append("select <include refid=\"" + this.BASE_COLUMN_LIST + "\" /> \n");
		sb.append(indent(level2)).append("from ").append(introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\n");
		sb.append(indent(level2) + "<where>\n");
		sb.append(indent(level3) + "<include refid=\"" + this.DYNAMIC_SQL_ID + "\" />\n");
		sb.append(indent(level2) + "</where>\n");
		sb.append(indent(level2) + "limit 1");
		org.mybatis.generator.api.dom.xml.TextElement text = new org.mybatis.generator.api.dom.xml.TextElement(sb.toString());
		element.addElement(text);

		document.getRootElement().addElement(element);
	}
}
