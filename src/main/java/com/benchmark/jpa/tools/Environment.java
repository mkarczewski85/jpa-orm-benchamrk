package com.benchmark.jpa.tools;

import org.openjdk.jmh.annotations.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@State(Scope.Benchmark)
public class Environment {

    @Param(value = "POSTGRE_SQL")
//    @Param(value = "MARIA_DB")
    private Database database;
    @Param(value = {"1000", "10000", "50000"})
    private int totalRows;

    private DataSource dataSource;

    @Setup
    public void init() throws SQLException {
        dataSource = DataSourceProvider.getDataSource(database);
        try (Connection connection = dataSource.getConnection()) {
            SqlBootstrap.createAndBootstrapDbTable(connection, totalRows);
        }
    }

    @TearDown(Level.Trial)
    public void clear() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            SqlBootstrap.clearTables(connection);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Database getDatabase() {
        return database;
    }

    public int getTotalRows() {
        return totalRows;
    }
}
