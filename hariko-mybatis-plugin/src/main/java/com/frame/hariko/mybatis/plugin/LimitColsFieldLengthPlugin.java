package com.frame.hariko.mybatis.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class LimitColsFieldLengthPlugin extends MyBatisPlugin {
	private String colsName = "remark,note";

	private String tblsName = "t_fund_trans";

	public boolean validate(List<String> warnings) {
		this.colsName = this.properties.getProperty("colsName");
		this.tblsName = this.properties.getProperty("tblsName");
		boolean valid = (StringUtility.stringHasValue(this.colsName)) || (StringUtility.stringHasValue(this.tblsName));
		if (!valid) {
			warnings.add(Messages.getString("ValidationError.18", "LimitColsFieldLengthPlugin", "colsName or tblsName"));
		}

		return valid;
	}

	public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
		String colName = introspectedColumn.getActualColumnName();
		colName = colName.toLowerCase();
		this.colsName = this.colsName.toLowerCase();

		String tblName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
		tblName = tblName.toLowerCase();
		this.tblsName = this.tblsName.toLowerCase();

		if ((this.tblsName.contains(tblName)) && (this.colsName.contains(colName)) && (introspectedColumn.isStringColumn())) {
			Parameter parameter = (Parameter) method.getParameters().get(0);
			String paraName = parameter.getName();
			int length = introspectedColumn.getLength();
			Collection<String> lines = new ArrayList<String>();
			lines.add("if(" + paraName + "!=null &&" + paraName + ".length() > " + length + "){");
			lines.add(paraName + "=" + paraName + ".substring(0," + length + ");");
			lines.add("}");
			method.addBodyLines(0, lines);
		}
		return true;
	}
}
