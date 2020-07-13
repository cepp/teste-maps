package br.com.cepp.maps.financas.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Profile({"dev", "test"})
    public DataSource getDataSource(@Value("${spring.datasource.username}") String username,
                                     @Value("${spring.datasource.password}") String password,
                                     @Value("${spring.datasource.url}") String url,
                                     @Value("${spring.datasource.driverClassName}") String driverClassName) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("default")
    public DataSource getDataSource(@Value("${spring.datasource.driverClassName}") String driverClassName) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv("JDBC_DATABASE_URL"));
        config.setDriverClassName(driverClassName);
        return new HikariDataSource(config);
    }
}
