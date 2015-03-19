package info.takebo.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * renmae [Mapper] to [Dao]
 */
public class MapperClassNamePlugin extends PluginAdapter {

	/**
	 * This plugin is always valid - no properties are required
	 */
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable table) {
		super.initialized(table);

		{
			String name = table.getMyBatis3JavaMapperType();
			table.setMyBatis3JavaMapperType(name.replaceAll("Mapper", "Dao"));
		}

		{
			String name = table.getDAOImplementationType();
			table.setDAOImplementationType(name.replaceAll("DAO", "Dao"));
		}

		{
			String name = table.getDAOInterfaceType();
			table.setDAOInterfaceType(name.replaceAll("DAO", "Dao"));
		}

		{
			String name = table.getMyBatis3XmlMapperFileName();
			table.setMyBatis3XmlMapperFileName(name.replaceAll("Mapper", "Dao"));
		}
	}
}