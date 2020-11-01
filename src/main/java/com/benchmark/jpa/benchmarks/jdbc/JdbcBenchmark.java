package com.benchmark.jpa.benchmarks.jdbc;

import com.benchmark.jpa.tools.Environment;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class JdbcBenchmark {


    private static final String SELECT_ALL_EMPLOYEE_QUERY =
            "SELECT id, firstname, lastname, emp_salary, emp_position, emp_date, id_team, id_address FROM employee LIMIT ?";

    private static final String SELECT_ALL_EMPLOYEE_WITH_ADDRESS_QUERY =
            "SELECT e.id, e.firstname, e.lastname, e.emp_salary, e.emp_position, e.emp_date, e.id_team, " +
                    "a.street, a.number, a.city, a.postal_code FROM employee AS e INNER JOIN address as a " +
                    "ON a.id = e.id_address LIMIT ?";

    private static final String SELECT_ALL_EMPLOYEES_BY_PROJECT_NAME =
            "SELECT e.id, e.firstname, e.lastname, e.emp_salary, e.emp_position, e.emp_date, e.id_team, e.id_address " +
                    "FROM employee e INNER JOIN employee_project ep ON ep.id_employee = e.id INNER JOIN project p ON p.id = ep.id_project " +
                    "WHERE p.proj_name LIKE ?";

    private static final String SELECT_ALL_EMPLOYEES_BY_TEAM_NAME =
            "SELECT e.id, e.firstname, e.lastname, e.emp_salary, e.emp_position, e.emp_date, e.id_team, e.id_address " +
                    "FROM employee e INNER JOIN team t ON t.id = e.id_team WHERE t.team_name LIKE ?";

    private static final String SELECT_ALL_EMPLOYEES_BY_TEAM_NAME_OPT =
            "SELECT e.id, e.firstname, e.lastname, e.emp_salary, e.emp_position, e.emp_date, e.id_team, e.id_address " +
                    "FROM employee e WHERE t.id_team = (SELECT id FROM team WHERE name LIKE ?)";

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 4)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 1)
    public void measureSelectAllEmployees(Environment env, Blackhole blackhole) throws SQLException {
        try (Connection connection = env.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLOYEE_QUERY)) {
                ps.setInt(1, env.getTotalRows());
                ResultSet rs = ps.executeQuery();
                blackhole.consume(rs);
            }
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 4)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 1)
    public void measureSelectAllEmployeesWithAddress(Environment env, Blackhole blackhole) throws SQLException {
        try (Connection connection = env.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLOYEE_WITH_ADDRESS_QUERY)) {
                ps.setInt(1, env.getTotalRows());
                ResultSet rs = ps.executeQuery();
                blackhole.consume(rs);
            }
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 4)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 1)
    public void measureSelectAllEmployeesByProjectName(Environment env, Blackhole blackhole) throws SQLException {
        try (Connection connection = env.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLOYEES_BY_PROJECT_NAME)) {
                ps.setString(1, "%" + env.getTotalRows());
                ResultSet rs = ps.executeQuery();
                blackhole.consume(rs);
            }
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 4)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 1)
    public void measureSelectAllEmployeesByTeamName(Environment env, Blackhole blackhole) throws SQLException {
        try (Connection connection = env.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLOYEES_BY_TEAM_NAME)) {
                ps.setString(1, "%" + env.getTotalRows());
                ResultSet rs = ps.executeQuery();
                blackhole.consume(rs);
            }
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 4)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 1)
    public void measureSelectAllEmployeesByTeamNameOptimized(Environment env, Blackhole blackhole) throws SQLException {
        try (Connection connection = env.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLOYEES_BY_TEAM_NAME_OPT)) {
                ps.setString(1, "%" + env.getTotalRows());
                ResultSet rs = ps.executeQuery();
                blackhole.consume(rs);
            }
        }
    }

}
