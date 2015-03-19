package info.takebo.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * add <code>select last_insert_id()</code> on insert method.
 */
public class LastInsertIdPlugin extends PluginAdapter {

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		element.addElement(this.getLastInsertIdElement());
		return super.sqlMapInsertElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		element.addElement(this.getLastInsertIdElement());
		return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		addOptionAnnotation(method, interfaze);
		return super.clientInsertMethodGenerated(method, interfaze, introspectedTable);
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		addOptionAnnotation(method, interfaze);
		return super.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
	}

	private void addOptionAnnotation(Method method, Interface interfaze) {
		FullyQualifiedJavaType importedType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options");
		interfaze.addImportedType(importedType);
		String annotation = "@Options(useGeneratedKeys = true, keyProperty = \"id\", keyColumn = \"id\")";
		method.addAnnotation(annotation);
	}

	private XmlElement getLastInsertIdElement() {
		XmlElement selectKeyElement = new XmlElement("selectKey");
		selectKeyElement.addAttribute(new Attribute("resultType", "java.lang.Integer"));
		selectKeyElement.addAttribute(new Attribute("order", "AFTER"));
		selectKeyElement.addAttribute(new Attribute("keyProperty", "userId")); // change "userId" to AutoIncrement Property
		selectKeyElement.addElement(new TextElement("select LAST_INSERT_ID()"));
		return selectKeyElement;
	}

	/**
	 * This plugin is always valid - no properties are required
	 */
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
}