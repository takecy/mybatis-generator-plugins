package info.takebo.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * <pre>
 * add pagination using mysql limit offset.
 * This class is only used in ibator code generator.
 * </pre>
 */
public class PaginationPlugin extends PluginAdapter {

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// add field, getter, setter for limit,offset clause
		addExampleField(topLevelClass, introspectedTable, "addLimit", "Boolean.TRUE", Boolean.class);
		addExampleField(topLevelClass, introspectedTable, "limit", "Integer.valueOf(20)", Integer.class);
		addExampleField(topLevelClass, introspectedTable, "offset", "Integer.valueOf(0)", Integer.class);
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method,
																		TopLevelClass topLevelClass,
																		IntrospectedTable introspectedTable) {

		appendLimitOffsetForProvider(method, "offset", "limit");
		return super.providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}

	@Override
	public boolean providerSelectByExampleWithBLOBsMethodGenerated(Method method,
																	TopLevelClass topLevelClass,
																	IntrospectedTable introspectedTable) {

		appendLimitOffsetForProvider(method, "offset", "limit");
		return super.providerSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}

	private void appendLimitOffsetForProvider(Method method, String offsetName, String limitName) {
		List<String> lines = method.getBodyLines();
		lines.remove(lines.size() - 1);

		// if(example != null && example.getOffset() != null && example.getLimit() != null && example.getOffset() > -1 && example.getLimit > -1) {
		StringBuilder line1 = new StringBuilder();
		line1.append("if(")
				.append("example != null")
				.append("&& example.isAddLimit()")
				.append("&& example.get")
				.append(Character.toUpperCase(offsetName.charAt(0)))
				.append(offsetName.substring(1))
				.append("() != null")
				.append("&& example.get")
				.append(Character.toUpperCase(limitName.charAt(0)))
				.append(limitName.substring(1))
				.append("() != null")
				.append("&& example.get")
				.append(Character.toUpperCase(offsetName.charAt(0)))
				.append(offsetName.substring(1))
				.append("() >= 0")
				.append("&& example.get")
				.append(Character.toUpperCase(limitName.charAt(0)))
				.append(limitName.substring(1))
				.append("() > 0")
				.append(") {");

		// return SQL() + "limit" + example.getOffset() + "," example.getLimit()
		StringBuilder line2 = new StringBuilder();
		line2.append("return SQL() + \" limit \" + example.get")
				.append(Character.toUpperCase(offsetName.charAt(0)))
				.append(offsetName.substring(1))
				.append("()+ \",\" + example.get")
				.append(Character.toUpperCase(limitName.charAt(0)))
				.append(limitName.substring(1))
				.append("();");

		lines.add(line1.toString());
		lines.add(line2.toString());
		lines.add("}");
		lines.add("return SQL();");
	}

	private void addExampleField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name, String initValue, Class<?> fieldClass) {
		CommentGenerator commentGenerator = context.getCommentGenerator();

		// add field
		{
			Field field = new Field();
			field.setVisibility(JavaVisibility.PROTECTED);
			field.setType(new FullyQualifiedJavaType(fieldClass.getName()));
			field.setName(name);
			if (initValue != null && initValue != "") {
				field.setInitializationString(initValue);
			}
			commentGenerator.addFieldComment(field, introspectedTable);
			topLevelClass.addField(field);
		}

		char c = name.charAt(0);
		String camel = Character.toUpperCase(c) + name.substring(1);

		// add setter
		{
			Method method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setName("set" + camel);
			method.addParameter(new Parameter(new FullyQualifiedJavaType(fieldClass.getName()), name));
			method.addBodyLine("this." + name + "=" + name + ";");
			commentGenerator.addGeneralMethodComment(method, introspectedTable);
			topLevelClass.addMethod(method);
		}

		// add getter
		{
			Method method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setReturnType(new FullyQualifiedJavaType(fieldClass.getName()));
			if (fieldClass == Boolean.class) {
				method.setName("is" + camel);
			} else {
				method.setName("get" + camel);
			}
			method.addBodyLine("return " + name + ";");
			commentGenerator.addGeneralMethodComment(method, introspectedTable);
			topLevelClass.addMethod(method);
		}
	}

	/**
	 * This plugin is always valid - no properties are required
	 */
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
}