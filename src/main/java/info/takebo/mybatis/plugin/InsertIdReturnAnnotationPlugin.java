package info.takebo.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * add <code>@Option</code> annotaion to insert method.
 */
public class InsertIdReturnAnnotationPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable table) {
		super.initialized(table);
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		addOptionAnnotation(method);
		return super.clientInsertMethodGenerated(method, interfaze, introspectedTable);
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		addOptionAnnotation(method);
		return super.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (interfaze != null)
			interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	private void addOptionAnnotation(Method method) {
		String annotation = "@Options(useGeneratedKeys = true, keyProperty = \"id\", keyColumn = \"id\")";
		method.addAnnotation(annotation);
	}

}