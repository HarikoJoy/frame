package com.frame.hariko.mybatis.plugin;

import java.util.List;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class AddAnnotationsAndImportsPlugin extends MyBatisPlugin {
	private String imports = "";
	private String annotations = "";

	public boolean validate(List<String> warnings) {
		this.imports = this.properties.getProperty("imports");
		this.annotations = this.properties.getProperty("annotations");
		boolean valid = (StringUtility.stringHasValue(this.imports)) || (StringUtility.stringHasValue(this.annotations));

		if (!valid) {
			warnings.add(Messages.getString("ValidationError.18", "AddAnnotationPlugin", "imports or annotations"));
		}

		return valid;
	}

	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (StringUtility.stringHasValue(this.imports)) {
			String[] imp = this.imports.split(",");
			for (String s : imp) {
				FullyQualifiedJavaType type = new FullyQualifiedJavaType(s);
				interfaze.addImportedType(type);
			}
		}

		if (StringUtility.stringHasValue(this.annotations)) {
			String[] ann = this.annotations.split(",");
			for (String s : ann) {
				interfaze.addAnnotation(s);
			}
		}

		return true;
	}
}
