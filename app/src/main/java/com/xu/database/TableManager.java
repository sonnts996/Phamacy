package com.xu.database;

public abstract class TableManager extends SQLMaker {

    public abstract String getTableName();

    public abstract void createTable();

    public abstract void addColumn(String newColumn, String columnType);

    public abstract XCursor selectTable();

    public abstract XCursor selectTable(String condition);

    public abstract XCursor selectTable(String column, String condition);

    /**
     * delete this table, after that, all function will be error;
     */
    public abstract void deleteTable();
}
