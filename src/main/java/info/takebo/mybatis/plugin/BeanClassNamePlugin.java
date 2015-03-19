package info.takebo.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * Rename Entity Class name
 */
public class BeanClassNamePlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable table) {
		super.initialized(table);
		String name = table.getBaseRecordType();
		table.setBaseRecordType(name + "Dto");
	}
}