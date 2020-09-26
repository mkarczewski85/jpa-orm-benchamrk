package com.benchmark.jpa.tools;

class SqlBootstrapHelper {

    static String composeDeleteQuery(String tableName) {
        return "delete from " + tableName;
    }

    static String composeCountQuery(String tableName) {
        return "select count(*) from " + tableName;
    }

    static String composeInsertQuery(String tableName, int parameters) {
        StringBuilder sb = new StringBuilder().append("insert into ").append(tableName).append(" values(");
        for (int i = 0; i < parameters; i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 2, sb.length()).append(")");
        return sb.toString();
    }

}
