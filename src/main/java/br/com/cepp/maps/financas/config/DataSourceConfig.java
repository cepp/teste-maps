package br.com.cepp.maps.financas.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource(@Value("${spring.datasource.username}") String username,
                                     @Value("${spring.datasource.password}") String password,
                                     @Value("${spring.datasource.url}") String url,
                                     @Value("${spring.datasource.driverClassName}") String driverClassName) {
        final String dbUrl = System.getenv("JDBC_DATABASE_URL");
        final String dbUsername = System.getenv("JDBC_DATABASE_USERNAME");
        final String dbPassword = System.getenv("JDBC_DATABASE_PASSWORD");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Strings.isEmpty(dbUrl) ? url : dbUrl);
        config.setUsername(Strings.isEmpty(dbUsername) ? username : dbUsername);
        config.setPassword(Strings.isEmpty(dbPassword) ? password : dbPassword);
        config.setDriverClassName(driverClassName);
        return new HikariDataSource(config);
    }
}
