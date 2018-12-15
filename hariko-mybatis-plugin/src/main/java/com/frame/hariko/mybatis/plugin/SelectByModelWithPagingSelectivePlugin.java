 package com.frame.hariko.mybatis.plugin;
 
 import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
 
 public class SelectByModelWithPagingSelectivePlugin extends MyBatisPlugin
 {
   private final String methodName = "selectByModelWithPagingSelective";
   private String db = "mysql";
   
 
 
 
 
   public boolean validate(List<String> warnings)
   {
     this.db = (this.properties.getProperty("db") != null ? this.properties.getProperty("db") : "mysql");
     
     return true;
   }
   
 
 
 
   public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
   {
     genSelectByModelWithPagingSelectiveMethod(interfaze, topLevelClass, introspectedTable);
     return true;
   }
   
 
 
 
   public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable)
   {
     if ("mysql".equalsIgnoreCase(this.db)) {
       genSelectByModelWithPagingMysqlSql(document, introspectedTable);
     } else if ("oracle".equalsIgnoreCase(this.db)) {
       genSelectByModelWithPagingOracleSql(document, introspectedTable);
     }
     return true;
   }
   
 
 
 
 
 
 
 
 
 
   private void genSelectByModelWithPagingMysqlSql(Document document, IntrospectedTable introspectedTable)
   {
     XmlElement element = new XmlElement("select");
     getClass();Attribute attribute = new Attribute("id", "selectByModelWithPagingSelective");
     element.addAttribute(attribute);
     attribute = new Attribute("parameterType", "map");
     element.addAttribute(attribute);
     attribute = new Attribute("resultMap", "BaseResultMap");
     element.addAttribute(attribute);
     this.context.getCommentGenerator().addComment(element);
     
     StringBuilder sb = new StringBuilder();
     int level2 = 2;
     int level3 = 3;
     
     sb.append("select <include refid=\"" + this.BASE_COLUMN_LIST + "\" /> from  ");
     sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime()).append("\n");
     sb.append(indent(level2) + "<where>\n");
     sb.append(indent(level3) + "<include refid=\"" + this.DYNAMIC_SQL_ID + "\" />\n");
     sb.append(indent(level2) + "</where> \n");
     sb.append(indent(level3) + " limit  #{start,jdbcType=DECIMAL}, #{pageSize,jdbcType=DECIMAL} \n");
     
     TextElement text = new TextElement(sb.toString());
     element.addElement(text);
     
     document.getRootElement().addElement(element);
   }
   
 
 
 
 
 
 
 
 
   private void genSelectByModelWithPagingSelectiveMethod(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
   {
     String modelName = introspectedTable.getTableConfiguration().getDomainObjectName();
     Method method = null;
     
     if ("mysql".equalsIgnoreCase(this.db)) {
       method = genMysqlMapperMethod(modelName);
     } else if ("oracle".equalsIgnoreCase(this.db)) {
       method = genOracleMapperMethod(modelName);
     }
     
     genSelectByModelWithPagingSelectiveMethodComment(method, introspectedTable);
     interfaze.addMethod(method);
   }
   
 
 
 
 
 
   private Method genMysqlMapperMethod(String modelName)
   {
     getClass();Method method = new Method(methodName);
     method.setVisibility(JavaVisibility.PUBLIC);
     method.setReturnType(new FullyQualifiedJavaType("List<" + modelName + ">"));
     Parameter parameter = new Parameter(new FullyQualifiedJavaType(modelName), "cond", "@Param(\"cond\")");
     method.addParameter(parameter);
     parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "start", "@Param(\"start\")");
     method.addParameter(parameter);
     parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "pageSize", "@Param(\"pageSize\")");
     method.addParameter(parameter);
     parameter = new Parameter(new FullyQualifiedJavaType("java.lang.boolean"), "strict", "@Param(\"strict\")");
     method.addParameter(parameter);
     
     return method;
   }
   
 
 
 
 
 
 
 
   private Method genOracleMapperMethod(String modelName)
   {
     getClass();Method method = new Method("selectByModelWithPagingSelective");
     method.setVisibility(JavaVisibility.PUBLIC);
     method.setReturnType(new FullyQualifiedJavaType("List<" + modelName + ">"));
     Parameter parameter = new Parameter(new FullyQualifiedJavaType(modelName), "cond", "@Param(\"cond\")");
     method.addParameter(parameter);
     parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "start", "@Param(\"start\")");
     method.addParameter(parameter);
     parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "end", "@Param(\"end\")");
     method.addParameter(parameter);
     parameter = new Parameter(new FullyQualifiedJavaType("java.lang.boolean"), "strict", "@Param(\"strict\")");
     method.addParameter(parameter);
     
     return method;
   }
   
 
 
 
   private void genSelectByModelWithPagingSelectiveMethodComment(Method method, IntrospectedTable introspectedTable)
   {
	   method.addJavaDocLine("/**");
     method.addJavaDocLine(" * 此方法返回记录为上不包含下包含即大于等于start,小于end;且如果start,end都为null则查询满足条件的所有记录.<br/>");
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
private void genSelectByModelWithPagingOracleSql(Document document, IntrospectedTable introspectedTable)
   {
     XmlElement element = new XmlElement("select");
     getClass();Attribute attribute = new Attribute("id", "selectByModelWithPagingSelective");
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
     
     sb.append("select <include refid=\"" + this.BASE_COLUMN_LIST + "\" /> from (\n ");
     sb.append(indent(level2) + "select <include refid=\"" + this.BASE_COLUMN_LIST + "\" /> , rownum rnum \n");
     sb.append(indent(level2) + "from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\n");
     sb.append(indent(level2) + "<where>\n");
     sb.append(indent(level3) + "<include refid=\"" + this.DYNAMIC_SQL_ID + "\" />\n");
     sb.append(indent(level3) + "<if test=\"end != null\">\n");
     sb.append(indent(level4) + "and rownum  &lt; #{end,jdbcType=DECIMAL}\n");
     sb.append(indent(level3) + "</if> \n");
     sb.append(indent(level2) + "</where> \n");
     sb.append(indent(level2) + ") a\n ");
     sb.append(indent(level1) + "<where>\n");
     sb.append(indent(level2) + "<if test=\"start != null\">\n");
     sb.append(indent(level3) + " rnum &gt;= #{start,jdbcType=DECIMAL} \n");
     sb.append(indent(level2) + "</if>\n");
     sb.append(indent(level1) + "</where>");
     
     TextElement text = new TextElement(sb.toString());
     element.addElement(text);
     
     document.getRootElement().addElement(element);
   }
 }


