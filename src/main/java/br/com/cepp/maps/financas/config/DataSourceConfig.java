package br.com.cepp.maps.financas.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Profile({"dev", "test"})
    public DataSource getDataSource(@Value("${spring.datasource.driverClassName}") String driverClassName,
                                    @Value("${spring.datasource.username}") String username,
                                    @Value("${spring.datasource.password}") String password,
                                    @Value("${spring.datasource.url}") String url){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

    @Bean
    @Profile(value = {"default"})
    public DataSource jndiDataSource(@Value("${spring.datasource.url}") String url,
                                     @Value("${spring.datasource.username}") String username,
                                     @Value("${spring.datasource.password}") String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
