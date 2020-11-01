package com.benchmark.jpa.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static com.benchmark.jpa.tools.SqlBootstrapHelper.*;

class SqlBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlBootstrap.class);
    private static final String DB_EMPLOYEE_TABLE = "employee";
    private static final String DB_ADDRESS_TABLE = "address";
    private static final String DB_PROJECT_TABLE = "project";
    private static final String DB_TEAM_TABLE = "team";
    private static final String DB_EMP_PROJECT_TABLE = "employee_project";

    static void createAndBootstrapDbTable(Connection connection, int numRows) throws SQLException {
        connection.setReadOnly(false);

        LOGGER.info("Bootstrapping table rows...");
        bootstrapSimpleNameFieldTable(connection, numRows / 10, DB_TEAM_TABLE);
        bootstrapSimpleNameFieldTable(connection, numRows / 10, DB_PROJECT_TABLE);
        bootstrapEmployeeAndAddressTable(connection, numRows);
        bootstrapEmployeeProjectTable(connection, numRows);
        connection.setReadOnly(true);
    }

    static void clearTables(Connection connection) {
        try (Statement st = connection.createStatement()) {
            st.execute(composeDeleteQuery(DB_EMP_PROJECT_TABLE));
            st.execute(composeDeleteQuery(DB_EMPLOYEE_TABLE));
            st.execute(composeDeleteQuery(DB_ADDRESS_TABLE));
            st.execute(composeDeleteQuery(DB_PROJECT_TABLE));
            st.execute(composeDeleteQuery(DB_TEAM_TABLE));
            LOGGER.info("Database cleared!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot clear tables", e);
        }
    }

    private static void bootstrapSimpleNameFieldTable(Connection connection, int numRows, String tableName) {
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(composeCountQuery(tableName));
            rs.next();
            LOGGER.info("Found {} rows in '{}' table", rs.getLong(1), tableName);

            if (rs.getLong(1) == numRows) {
                return;
            }
            int start = (int) rs.getLong(1) + 1;

            try (PreparedStatement ps = connection.prepareStatement(composeInsertQuery(tableName, 2))) {
                for (long i = start; i <= numRows; i++) {
                    ps.setLong(1, i);
                    ps.setString(2, tableName + "_name_" + i);
                    ps.addBatch();
                }
                ps.executeBatch();

                rs = st.executeQuery(composeCountQuery(tableName));
                rs.next();
                LOGGER.info("After bootstrap there are {} rows in '{}' table", rs.getLong(1), tableName);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Cannot setup %s table", tableName), e);
        }
    }

    private static void bootstrapEmployeeAndAddressTable(Connection connection, int numRows) {
        bootstrapAddressTable(connection, numRows);
        bootstrapEmployeeTable(connection, numRows);
    }

    private static void bootstrapAddressTable(Connection connection, int numRows) {
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(composeCountQuery(DB_ADDRESS_TABLE));
            rs.next();
            LOGGER.info("Found {} rows in '{}' table", rs.getLong(1), DB_ADDRESS_TABLE);

            if (rs.getLong(1) == numRows) {
                return;
            }
            int start = (int) rs.getLong(1) + 1;

            try (PreparedStatement ps = connection.prepareStatement(composeInsertQuery(DB_ADDRESS_TABLE, 5))) {
                for (int i = start; i <= numRows; i++) {
                    ps.setLong(1, i);
                    ps.setString(2, "street_" + i);
                    ps.setInt(3, i);
                    ps.setString(4, "city_" + i);
                    ps.setString(5, "code_" + i);
                    ps.addBatch();
                }
                ps.executeBatch();

                rs = st.executeQuery(composeCountQuery(DB_ADDRESS_TABLE));
                rs.next();
                LOGGER.info("After bootstrap there are {} rows in '{}' table", rs.getLong(1), DB_ADDRESS_TABLE);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Cannot setup %s table", DB_ADDRESS_TABLE), e);
        }
    }

    private static void bootstrapEmployeeTable(Connection connection, int numRows) {
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(composeCountQuery(DB_EMPLOYEE_TABLE));
            rs.next();
            LOGGER.info("Found {} rows in {} table", rs.getLong(1), DB_EMPLOYEE_TABLE);
            if (rs.getLong(1) == numRows) {
                return;
            }
            int start = (int) rs.getLong(1) + 1;

            try (PreparedStatement ps = connection.prepareStatement(composeInsertQuery(DB_EMPLOYEE_TABLE, 8))) {
                long teamId = 1;
                for (int i = start; i <= numRows; i++) {
                    ps.setLong(1, i);
                    ps.setString(2, "firstname_" + i);
                    ps.setString(3, "lastname_" + i);
                    ps.setDouble(4, generateRandomSalary());
                    ps.setString(5, "position_" + i);
                    ps.setDate(6, new Date(System.currentTimeMillis()));
                    ps.setLong(7, teamId++);
                    ps.setLong(8, i);
                    ps.addBatch();
                    if (teamId > numRows / 10) {
                        teamId = 1;
                    }
                }
                ps.executeBatch();

                rs = st.executeQuery(composeCountQuery(DB_EMPLOYEE_TABLE));
                rs.next();
                LOGGER.info("After bootstrap there are {} rows in '{}' table", rs.getLong(1), DB_EMPLOYEE_TABLE);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Cannot setup %s table", DB_EMPLOYEE_TABLE), e);
        }
    }

    private static void bootstrapEmployeeProjectTable(Connection connection, int numRows) {
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(composeCountQuery(DB_EMP_PROJECT_TABLE));
            rs.next();
            LOGGER.info("Found {} rows in '{}' table", rs.getLong(1), DB_EMP_PROJECT_TABLE);
            if (rs.getLong(1) == numRows) {
                return;
            }
            int start = (int) rs.getLong(1) + 1;

            try (PreparedStatement ps = connection.prepareStatement(composeInsertQuery(DB_EMP_PROJECT_TABLE, 2))) {
                long projectId = 1;
                for (int i = start; i <= numRows; i++) {
                    ps.setLong(1, i);
                    ps.setLong(2, projectId++);
                    ps.addBatch();
                    if (projectId > numRows / 10) {
                        projectId = 1;
                    }
                }
                ps.executeBatch();

                rs = st.executeQuery(composeCountQuery(DB_EMP_PROJECT_TABLE));
                rs.next();
                LOGGER.info("After bootstrap there are {} rows in '{}' table", rs.getLong(1), DB_EMP_PROJECT_TABLE);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(String.format("Cannot setup %s table", DB_EMP_PROJECT_TABLE), e);
        }
    }

    private static double generateRandomSalary() {
        return (Math.random() * ((20000 - 4000) + 1)) + 4000;
    }

}
