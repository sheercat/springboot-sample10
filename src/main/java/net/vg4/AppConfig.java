package net.vg4;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

@Configuration
public class AppConfig {
	@Autowired
	DataSourceProperties dataSourceProperties;
	DataSource dataSource;

	@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
	@Bean(destroyMethod = "close")
	DataSource reqlDataSource() {
		DataSourceBuilder factory = DataSourceBuilder.create(this.dataSourceProperties.getClassLoader())
				.url(this.dataSourceProperties.getUrl()).username(this.dataSourceProperties.getUsername())
				.password(this.dataSourceProperties.getPassword());
		this.dataSource = factory.build();
		return this.dataSource;
	}

	@Primary
	@Bean
	DataSource dataSource() {
		return new DataSourceSpy(this.dataSource);
	}
}
