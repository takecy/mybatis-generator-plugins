package info.takebo.mybatis.plugin;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * TODO
 */
public class GenerateSplitTableProviderPlugin extends PluginAdapter {

	private static final String KEY_OF_SERVICE_PROJECT = "serviceProject";
	private static final String KEY_OF_SERVICE_PACKAGE = "servicePackage";
	String serviceProject;
	String serviceBasePackage;

	@Override
	public boolean validate(List<String> warnings) {
		//		serviceProject = getProperties().getProperty(KEY_OF_SERVICE_PROJECT); //$NON-NLS-1$
		//		serviceBasePackage = getProperties().getProperty(KEY_OF_SERVICE_PACKAGE); //$NON-NLS-1$
		//
		// boolean valid = StringUtility.stringHasValue(serviceProject)
		// && StringUtility.stringHasValue(serviceBasePackage);
		//
		// if (!valid) {
		// if (!StringUtility.stringHasValue(serviceProject)) {
		//				warnings.add("GenerateAdditionalJavaFilesPluginï¼serviceProjectå±æ§éè¦è®¾ç½®"); //$NON-NLS-1$
		// }
		// if (!StringUtility.stringHasValue(serviceBasePackage)) {
		//				warnings.add("GenerateAdditionalJavaFilesPluginï¼servicePackageå±æ§éè¦è®¾ç½®"); //$NON-NLS-1$
		// }
		// }

		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

		ArrayList<GeneratedJavaFile> retn = new ArrayList<GeneratedJavaFile>();

		String daoBasePackage = this.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
		String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
		String DomainObjectNameWithLowerFirstChar = new StringBuilder().append(Character.toLowerCase(domainObjectName.charAt(0)))
																		.append(domainObjectName.substring(1)).toString();

		// dao-----------------------------------------
		String daoProject = this.getContext().getSqlMapGeneratorConfiguration().getTargetProject();

		// add dao interface
		// Interface daoInterface = getDaoInterface(introspectedTable, daoBasePackage, domainObjectName, DomainObjectNameWithLowerFirstChar);
		// GeneratedJavaFile daoInterfaceJavaFile = new GeneratedJavaFile(daoInterface, daoProject, new DefaultJavaFormatter());
		// retn.add(daoInterfaceJavaFile);

		// add dao impl class
		TopLevelClass daoImplClass = getDaoImplClass(introspectedTable, daoBasePackage, domainObjectName, DomainObjectNameWithLowerFirstChar);
		GeneratedJavaFile daoImplClassJavaFile = new GeneratedJavaFile(daoImplClass, daoProject, new DefaultJavaFormatter());
		retn.add(daoImplClassJavaFile);

		// service-------------------------------------
		// Interface serviceInterface = getServiceInterface(introspectedTable, serviceBasePackage, domainObjectName,
		// DomainObjectNameWithLowerFirstChar);
		// TopLevelClass serviceImplClass = getServiceImplClass(introspectedTable, serviceBasePackage, daoBasePackage, domainObjectName,
		// DomainObjectNameWithLowerFirstChar);
		// // add service interface
		// GeneratedJavaFile serviceInterfaceJavaFile = new GeneratedJavaFile(serviceInterface, serviceProject, new DefaultJavaFormatter());
		// retn.add(serviceInterfaceJavaFile);
		// // add service impl class
		// GeneratedJavaFile serviceImplClassJavaFile = new GeneratedJavaFile(serviceImplClass, serviceProject, new DefaultJavaFormatter());
		// retn.add(serviceImplClassJavaFile);

		return retn;
	}

	private Interface getDaoInterface(IntrospectedTable introspectedTable,
										String daoBasePackage,
										String domainObjectName,
										String DomainObjectNameWithLowerFirstChar) {
		String domainBasePackage = this.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
		// class
		Interface cls = new Interface(daoBasePackage + "." + domainObjectName + "Dao");
		cls.setVisibility(JavaVisibility.PUBLIC);
		cls.addSuperInterface(new FullyQualifiedJavaType(String.format("BaseDao<%s>", domainObjectName)));

		cls.addImportedType(new FullyQualifiedJavaType(String.format("%s.base.BaseDao", daoBasePackage)));
		cls.addImportedType(new FullyQualifiedJavaType(String.format("%s.%s", domainBasePackage, domainObjectName)));
		return cls;
	}

	private TopLevelClass getDaoImplClass(IntrospectedTable introspectedTable,
											String daoBasePackage,
											String domainObjectName,
											String DomainObjectNameWithLowerFirstChar) {
		String domainBasePackage = this.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
		// class
		TopLevelClass cls = new TopLevelClass(daoBasePackage + ".impl." + domainObjectName + "DaoImpl");
		cls.addAnnotation(String.format("@Repository(value=\"%sDao\")", DomainObjectNameWithLowerFirstChar));
		cls.setSuperClass(String.format("BaseDaoImpl<%s>", domainObjectName));
		cls.addSuperInterface(new FullyQualifiedJavaType(String.format("%sDao", domainObjectName)));
		cls.addImportedType("org.springframework.stereotype.Repository");
		cls.addImportedType(String.format("%s.%sDao", daoBasePackage, domainObjectName));
		cls.addImportedType(String.format("%s.base.impl.BaseDaoImpl", daoBasePackage));
		cls.addImportedType(String.format("%s.%s", domainBasePackage, domainObjectName));

		// fields
		Field nameSpaceField = new Field();
		nameSpaceField.setVisibility(JavaVisibility.PRIVATE);
		nameSpaceField.setStatic(true);
		nameSpaceField.setFinal(true);
		nameSpaceField.setType(FullyQualifiedJavaType.getStringInstance());
		nameSpaceField.setName("NAME_SPACE");
		nameSpaceField.setInitializationString("\"" + daoBasePackage + "." +
				introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper\"");
		cls.addField(nameSpaceField);

		// methods
		Method getNameSpace = new Method();
		getNameSpace.addAnnotation("@Override");
		getNameSpace.setVisibility(JavaVisibility.PROTECTED);
		getNameSpace.setName("getNameSpace");
		getNameSpace.setReturnType(FullyQualifiedJavaType.getStringInstance());
		getNameSpace.addBodyLine("return NAME_SPACE;");
		cls.addMethod(getNameSpace);
		return cls;
	}

	private Interface getServiceInterface(IntrospectedTable introspectedTable,
											String serviceBasePackage,
											String domainObjectName,
											String DomainObjectNameWithLowerFirstChar) {
		String domainBasePackage = this.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
		// class
		Interface cls = new Interface(serviceBasePackage + "." + domainObjectName + "Service");
		cls.setVisibility(JavaVisibility.PUBLIC);
		cls.addSuperInterface(new FullyQualifiedJavaType(String.format("BaseService<%s>", domainObjectName)));

		cls.addImportedType(new FullyQualifiedJavaType(String.format("%s.base.BaseService", serviceBasePackage)));
		cls.addImportedType(new FullyQualifiedJavaType(String.format("%s.%s", domainBasePackage, domainObjectName)));
		return cls;
	}

	private TopLevelClass getServiceImplClass(IntrospectedTable introspectedTable,
												String serviceBasePackage,
												String daoBasePackage,
												String domainObjectName,
												String DomainObjectNameWithLowerFirstChar) {
		String domainBasePackage = this.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
		// class
		TopLevelClass cls = new TopLevelClass(serviceBasePackage + ".impl." + domainObjectName + "ServiceImpl");
		cls.addAnnotation(String.format("@Service(value=\"%sService\")", DomainObjectNameWithLowerFirstChar));
		cls.addAnnotation("@Transactional(rollbackFor=Exception.class)");
		cls.setSuperClass(String.format("BaseServiceImpl<%s>", domainObjectName));
		cls.addSuperInterface(new FullyQualifiedJavaType(String.format("%sService", domainObjectName)));
		cls.addImportedType("javax.annotation.Resource");
		cls.addImportedType("org.springframework.stereotype.Service");
		cls.addImportedType("org.springframework.transaction.annotation.Transactional");
		cls.addImportedType(String.format("%s.%sDao", daoBasePackage, domainObjectName));
		cls.addImportedType(String.format("%s.base.BaseDao", daoBasePackage));
		cls.addImportedType(String.format("%s.base.impl.BaseServiceImpl", serviceBasePackage));
		cls.addImportedType(String.format("%s.%sService", serviceBasePackage, domainObjectName));
		cls.addImportedType(String.format("%s.%s", domainBasePackage, domainObjectName));
		// fields
		Field daoField = new Field();
		daoField.addAnnotation("@Resource");
		daoField.setVisibility(JavaVisibility.PRIVATE);
		daoField.setType(new FullyQualifiedJavaType(String.format("%sDao", domainObjectName)));
		daoField.setName(String.format("%sDao", DomainObjectNameWithLowerFirstChar));
		cls.addField(daoField);

		// methods
		Method getDao = new Method();
		getDao.addAnnotation("@Override");
		getDao.setVisibility(JavaVisibility.PROTECTED);
		getDao.setName("getDao");
		getDao.setReturnType(new FullyQualifiedJavaType(String.format("BaseDao<%s>", domainObjectName)));
		getDao.addBodyLine(String.format("return %sDao;", DomainObjectNameWithLowerFirstChar));
		cls.addMethod(getDao);

		return cls;
	}
}