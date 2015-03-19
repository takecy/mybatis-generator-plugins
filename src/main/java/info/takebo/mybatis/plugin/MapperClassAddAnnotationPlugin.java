package info.takebo.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * mapperへannotationを付加する
 */
public class MapperClassAddAnnotationPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if(interfaze != null) {
			interfaze.addImportedType(new FullyQualifiedJavaType("info.takebo.db.annotation.AppMapper"));
			interfaze.addAnnotation("@AppMapper");
		}
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

}