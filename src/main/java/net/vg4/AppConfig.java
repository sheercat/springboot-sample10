package net.vg4;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.val;

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

@Configuration
@val
public class AppConfig {
    final DataSourceProperties properties;
    DataSource dataSource;

    @Autowired
    public AppConfig(DataSourceProperties properties) {this.properties = properties;}

    @Bean(destroyMethod = "close")
    @ConfigurationProperties("spring.datasource")
    DataSource realDataSource() throws URISyntaxException {
        String url;
        String username;
        String password;

        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null) {
            val dbUri = new URI(databaseUrl);
            url = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
        } else {
            url = this.properties.getUrl();
            username = this.properties.getUsername();
            password = this.properties.getPassword();
        }

        val factory = DataSourceBuilder.create(this.properties.getClassLoader()).url(url)
                                       .username(username).password(password);
        this.dataSource = factory.build();
        return this.dataSource;
    }
}
