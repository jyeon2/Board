package com.example.board;

import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
	
	@Value("${spring.profiles.active:local}")
	private String activeProfile;
	
	@Value("${spring.datasource.hikari.jdbc-url:dummy}")
	private String appJdbcUrl;
	
	@Value("${spring.datasource.hikari.username:dummyuser}")
	private String appJdbcUser;
	
	@Value("${spring.datasource.hikari.password:dummypswd}")
	private String appJdbcPswd;
	
	@Value("${spring.datasource.hikari.driver-class-name:dummy.class}")
	private String appJdbcDriverClassName;
	
	@Value("${spring.datasource.jndi-name:none}")
	private String poolJndiName;
	
	//@Value("${koreanre.appl.path.sqlmap}")
	private final String sqlMapResourcesPath = "classpath:sql/**/*.xml";
	
	@Autowired
	private ApplicationContext context;
	
	
	@Bean(name = "applicationDataSource")
	public DataSource applicationDataSource() throws URISyntaxException {
		DataSource ds = null;
		
		// 1. Try Datasource with JNDI 
			logger.info("Set datasource using jdbc-url : {}", appJdbcUrl);
			DataSourceBuilder<?> builder = DataSourceBuilder.create();
			builder.driverClassName(appJdbcDriverClassName);  // org.postgresql.Driver
			builder.username(appJdbcUser);
			builder.password(appJdbcPswd);
			builder.url(appJdbcUrl);
			
			ds = builder.build();
		return ds;
	}
	
	@Bean(name = "applicationSqlSessionFactory")
	public SqlSessionFactory applicationSessionFactory(@Qualifier("applicationDataSource")DataSource applicationDataSource, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		
		sqlSessionFactoryBean.setDataSource(applicationDataSource);
		sqlSessionFactoryBean.setMapperLocations(
				applicationContext.getResources(sqlMapResourcesPath)
				);
		sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);	
		
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean(name = "applSqlSessionTemplate")
	public SqlSessionTemplate applSqlSessionTemplate(@Qualifier("applicationSqlSessionFactory")SqlSessionFactory applicationSessionFactory) throws Exception {
		return new SqlSessionTemplate(applicationSessionFactory);
	}
}
