package com.frame.hariko.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;

public class RenameFileNamePlugin extends MyBatisPlugin {
	private String prefix = "";
	private String suffix = "_SqlMap.xml";

	public boolean validate(List<String> warnings) {
		this.prefix = (this.properties.getProperty("prefix") == null ? this.prefix : this.properties.getProperty("prefix"));
		this.suffix = (this.properties.getProperty("suffix") == null ? this.suffix : this.properties.getProperty("suffix"));

		return true;
	}

	public void initialized(IntrospectedTable introspectedTable) {
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();

		String tableName = table.getDomainObjectName();

		introspectedTable.setMyBatis3XmlMapperFileName(this.prefix + tableName + this.suffix);
	}

	public static void main(String[] args) {
		String tableName = "T_USER";
		System.out.print(tableName.replace("T_", ""));
	}
}
