package com.benchmark.jpa.tools;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

class DataSourceProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceProvider.class);

    static DataSource getDataSource(Database type) {
        switch (type) {
            case MARIA_DB:
                LOGGER.info(">>>> Data source: MariaDB");
                return mariaDBDataSource();
            case POSTGRE_SQL:
                LOGGER.info(">>>> Data source: PostgreSQL");
                return postgreSQLDataSource();
            default:
                throw new IllegalArgumentException("Requested data source is unavailable");
        }
    }

    private static HikariDataSource mariaDBDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/staff");
        config.setUsername("root");
        config.setPassword("matkal13");
        return new HikariDataSource(config);
    }

    private static HikariDataSource postgreSQLDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("url", "jdbc:postgresql://localhost:5432/staff");
        config.addDataSourceProperty("user", "postgres");
        config.addDataSourceProperty("password", "matkal13");
        return new HikariDataSource(config);
    }

}
