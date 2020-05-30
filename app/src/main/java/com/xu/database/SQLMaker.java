package com.xu.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class SQLMaker {
    /**
     * check value exited in table
     *
     * @param table  table need check
     * @param column column contain value
     * @param value  value need check
     * @return is exited
     */
    public boolean checkExited(SQLiteDatabase database, String table, String column, String value) {
        try {
            Cursor cursor = database.rawQuery(makeSQLSelectTable(table, makeCondition(column, value)), null);
            boolean val = (cursor.getCount() != 0);
            cursor.close();
            return val;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
            return false;
        }
    }


    /**
     * print table data to log
     *
     * @param cursor respond of query
     */
    public void printData(String dataName, XCursor cursor) {
        Log.e("DATABASE", dataName);
        if (cursor == null) {
            Log.e("DATABASE", "null data");
            return;
        }

        String format = "%-10s\t";
        Log.i("NUMBER ROW", String.valueOf(cursor.getCount()));
        StringBuilder col = new StringBuilder();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String value = cursor.getColumnName(i);
            col.append(value.length() >= 10 ? value.substring(0, 8).concat("..\t") : String.format(format, value));
        }
        Log.i("ROW", col.toString());
        if (cursor.getCount() <= 0) {
            Log.i("ROW", "No row founded");
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String value = cursor.getString(i);
                if (value == null)
                    row.append(String.format(format, "null"));
                else
                    row.append(value.length() >= 10 ? value.substring(0, 8).concat("..\t") : String.format(format, value));
            }
            Log.i(String.format("%3d", cursor.getPosition()), row.toString());
            cursor.moveToNext();
        }
    }

    public void deleteTable(SQLiteDatabase database, String tableName) {
        database.execSQL("DROP TABLE " + tableName + ";");
        Log.w(tableName, "deleted!!!");
    }

    public String makeSQLCreateTable(String tableName) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (ID INTEGER PRIMARY KEY AUTOINCREMENT);", tableName);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLCreateTable(String tableName, String columnID) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT);", tableName, columnID);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLCreateTable(String tableName, String... columnList) {
        String cols = makeList(true, columnList);
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tableName, cols);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLCreateColumn(String tableName, String column, String dataType) {
        String sql = String.format("ALTER TABLE %s ADD %s %s;", tableName, column, dataType);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLInsertTable(String tableName, String column, String value) {
        String sql = makeSQLInsertTable(tableName, column, value, true);
        Log.i("SQL ", sql);
        return sql;
    }

    /**
     * @param tableName   table name
     * @param column      list column name
     * @param value       list corresponding column value
     * @param isMakeValue is make list value, default is true
     * @return sql excuse
     */
    public String makeSQLInsertTable(String tableName, String column, String value, boolean isMakeValue) {
        String vals = value;
        if (isMakeValue) {
            vals = makeList(value);
        }
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, column, vals);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLInsertTable(String tableName, String[] columns, String[] values) {
        if (columns.length != values.length)
            throw new RuntimeException("Number column and number value not match!");
        String cols = makeList(true, columns);
        String vals = makeList(values);
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, cols, vals);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLInsertTable(String tableName, Line line) {
        List<MapData> data = line.get();
        String cols = "";
        String vals = "";
        for (MapData m : data) {
            if (cols.equals("")) {
                cols = cols.concat(makeList(true, m.getKey()));
            } else {
                cols = cols.concat(",").concat(makeList(true, m.getKey()));
            }
            if (vals.equals("")) {
                vals = vals.concat(makeList(m.getValue()));
            } else {
                vals = vals.concat(",").concat(makeList(m.getValue()));
            }
        }

        return makeSQLInsertTable(tableName, cols, vals, false);
    }

    public String makeSQLSelectTable(String tableName) {
        String sql = String.format("SELECT * FROM %s;", tableName);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLSelectTable(String tableName, String condition) {
        String sql;
        if (condition.equals("")) {
            sql = makeSQLSelectTable(tableName);
        } else {
            sql = String.format("SELECT * FROM %s WHERE %s;", tableName, condition);
        }
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLSelectTable(String tableName, String column, String condition) {
        String sql;
        if (condition.equals("")) {
            sql = String.format("SELECT %s FROM %s;", column, tableName);
        } else {
            sql = String.format("SELECT %s FROM %s WHERE %s;", column, tableName, condition);
        }
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLAddColumn(String tableName, String newColumn, String columnType){
        String sql;
        sql = String.format("ALTER TABLE %s ADD COLUMN %s %s",tableName, newColumn, columnType);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLUpdate(String tableName, String columnsValues) {
        String sql = String.format("UPDATE %s SET %s", tableName, columnsValues);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLUpdate(String tableName, String columnsValue, String condition) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", tableName, columnsValue, condition);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeSQLUpdate(String tableName, Line line, String condition) {
        List<MapData> data = line.get();
        String list = "";
        for (MapData m : data) {
            if (list.equals("")) {
                list = list.concat(makeList(true, makeCondition(m.getKey(), m.getValue())));
            } else {
                list = list.concat(",").concat(makeList(true, makeCondition(m.getKey(), m.getValue())));
            }
        }

        String sql = makeSQLUpdate(tableName, list, condition);
        Log.i("SQL ", sql);
        return sql;
    }

    public String makeCondition(String column, String value) {
        return makeCondition(column, value, false);
    }

    public String makeCondition(String column, int value) {
        return makeCondition(column, String.valueOf(value));
    }

    public String makeCondition(String column, String value, boolean isColumn) {
        if (isColumn) {
            String sql;
            sql = column.concat("=").concat(value);
            return sql;
        } else {
            String sql;
            if (isNaN(value)) {
                sql = column.concat("='").concat(value).concat("'");
                return sql;
            } else {
                sql = column.concat("=").concat(value);
                return sql;
            }
        }
    }

    public String makeSum(String column) {
        String sql = String.format("SUM(%s)", column);
        return sql;
    }

    public String makeLike(String column, String value) {
        return String.format("%s LIKE '%%%s%%'  COLLATE NOCASE", column, value);
    }


    public String makeMax(String column) {
        String sql = String.format("MAX(%s)", column);
        return sql;
    }

    public String makeCount(String column) {
        String sql = String.format("COUNT(%s)", column);
        return sql;
    }

    public String makeOr(String... conditions) {
        String sql = "";
        for (String c : conditions) {
            sql = sql.concat(c).concat(" OR ");
        }
        if (sql.endsWith(" OR ")) {
            sql = sql.substring(0, sql.lastIndexOf(" OR "));
        }
        return sql;
    }

    public String makeAnd(String... conditions) {
        String sql = "";
        for (String c : conditions) {
            sql = sql.concat(c).concat(" AND ");
        }
        if (sql.endsWith(" AND ")) {
            sql = sql.substring(0, sql.lastIndexOf(" AND "));
        }
        return sql;
    }

    public String[] exceptIDColumn(String[] columns) {
        return Arrays.copyOfRange(columns, 1, columns.length);
    }

    public String makeList(String... args) {
        return makeList(false, args);
    }

    public String makeList(boolean isColumn, String... args) {
        String vals = "";
        for (String s : args) {
            if (s == null) continue;
            if (isColumn) {
                vals = vals.concat(s).concat(",");
            } else {
                if (isNaN(s)) {
                    vals = vals.concat("'").concat(s).concat("',");
                } else {
                    vals = vals.concat(s).concat(",");
                }
            }
        }
        if (vals.endsWith(",")) vals = vals.substring(0, vals.lastIndexOf(","));
        return vals;
    }

    public boolean isNaN(String n) {
        return !n.matches("-?\\d+(\\.\\d+)?");
    }

    public String makePointTo(String tableName, String column) {
        return tableName.concat(".").concat(column);
    }

    public String makeSelectJoin(String table1, String table2, String columns, String condition, JoinType type) {
        String sql = "";
        switch (type) {
            case LEFT:
            case RIGHT:
            case INNER:
                sql = String.format("SELECT %s FROM %s %s JOIN %s ON %s", columns, table1, type.toString(), table2, condition);
                break;
            case CROSS:
            case NATURAL:
                sql = String.format("SELECT %s FROM %s %s JOIN %s", columns, table1, type.toString(), table2);
            case OUTER:
                sql = String.format("SELECT %s FROM %s FULL OUTER JOIN %s ON %s", columns, table1, table2, condition);
        }
        return sql;
    }

    public enum JoinType {
        INNER, LEFT, RIGHT, OUTER, NATURAL, CROSS

    }
}
