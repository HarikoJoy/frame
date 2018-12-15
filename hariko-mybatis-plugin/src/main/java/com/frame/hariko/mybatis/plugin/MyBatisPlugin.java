package com.frame.hariko.mybatis.plugin;

import java.util.Date;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.JavaElement;

abstract class MyBatisPlugin extends PluginAdapter {
	protected String DYNAMIC_SQL_ID = "dynamicModelSql";

	protected String BASE_COLUMN_LIST = "Base_Column_List";

	protected String indent(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
		javaElement.addJavaDocLine(" *");
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append("@hariko.generated");
		if (markAsDoNotDelete) {
			sb.append(" do_not_delete_during_merge");
		}
		String s = getDateString();
		if (s != null) {
			sb.append(' ');
			sb.append(s);
		}
		javaElement.addJavaDocLine(sb.toString());
	}

	protected String getDateString() {
		return new Date().toString();
	}
}
