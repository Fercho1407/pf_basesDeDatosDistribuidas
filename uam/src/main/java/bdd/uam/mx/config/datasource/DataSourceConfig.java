package bdd.uam.mx.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties({
    RuralDataSourceProperties.class,
    SuburbanoDataSourceProperties.class,
    UrbanoDataSourceProperties.class
})
public class DataSourceConfig {

    @Bean
    public DataSource ruralDataSource(RuralDataSourceProperties props) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(props.getUrl());
        ds.setUsername(props.getUsername());
        ds.setPassword(props.getPassword());
        ds.setDriverClassName(props.getDriverClassName());
        return ds;
    }

    @Bean
    public DataSource suburbanaDataSource(SuburbanoDataSourceProperties props) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(props.getUrl());
        ds.setUsername(props.getUsername());
        ds.setPassword(props.getPassword());
        ds.setDriverClassName(props.getDriverClassName());
        return ds;
    }

    @Bean
    public DataSource urbanaDataSource(UrbanoDataSourceProperties props) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(props.getUrl());
        ds.setUsername(props.getUsername());
        ds.setPassword(props.getPassword());
        ds.setDriverClassName(props.getDriverClassName());
        return ds;
    }

    @Primary
    @Bean
    public DataSource dataSource(
            DataSource ruralDataSource,
            DataSource suburbanaDataSource,
            DataSource urbanaDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(Zona.RURAL, ruralDataSource);
        targetDataSources.put(Zona.SUBURBANA, suburbanaDataSource);
        targetDataSources.put(Zona.URBANA, urbanaDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(ruralDataSource);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }
}