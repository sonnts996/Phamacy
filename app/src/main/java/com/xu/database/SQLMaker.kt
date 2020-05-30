package com.xu.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log

import java.util.Arrays

open class SQLMaker {
    /**
     * check value exited in table
     *
     * @param table  table need check
     * @param column column contain value
     * @param value  value need check
     * @return is exited
     */
    fun checkExited(database: SQLiteDatabase, table: String, column: String, value: String): Boolean {
        return try {
            val cursor = database.rawQuery(makeSQLSelectTable(table, makeCondition(column, value)), null)
            val `val` = cursor.count != 0
            cursor.close()
            `val`
        } catch (sql_ex: SQLiteException) {
            sql_ex.printStackTrace()
            false
        }

    }


    /**
     * print table data to log
     *
     * @param cursor respond of query
     */
    fun printData(dataName: String, cursor: XCursor?) {
        Log.e("DATABASE", dataName)
        if (cursor == null) {
            Log.e("DATABASE", "null data")
            return
        }

        val format = "%-10s\t"
        Log.i("NUMBER ROW", cursor.count.toString())
        val col = StringBuilder()
        for (i in 0 until cursor.columnCount) {
            val value = cursor.getColumnName(i)
            col.append(if (value.length >= 10) value.substring(0, 8) + "..\t" else String.format(format, value))
        }
        Log.i("ROW", col.toString())
        if (cursor.count <= 0) {
            Log.i("ROW", "No row founded")
            return
        }

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val row = StringBuilder()
            for (i in 0 until cursor.columnCount) {
                val value = cursor.getString(i)
                if (value == null)
                    row.append(String.format(format, "null"))
                else
                    row.append(if (value.length >= 10) value.substring(0, 8) + "..\t" else String.format(format, value))
            }
            Log.i(String.format("%3d", cursor.position), row.toString())
            cursor.moveToNext()
        }
    }

    fun deleteTable(database: SQLiteDatabase, tableName: String) {
        database.execSQL("DROP TABLE $tableName;")
        Log.w(tableName, "deleted!!!")
    }

    fun makeSQLCreateTable(tableName: String): String {
        val sql = "CREATE TABLE IF NOT EXISTS $tableName (ID INTEGER PRIMARY KEY AUTOINCREMENT);"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLCreateTable(tableName: String, columnID: String): String {
        val sql = "CREATE TABLE IF NOT EXISTS $tableName ($columnID INTEGER PRIMARY KEY AUTOINCREMENT);"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLCreateTable(tableName: String, vararg columnList: String): String {
        val cols = makeList(true, *columnList)
        val sql = "CREATE TABLE IF NOT EXISTS $tableName ($cols);"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLCreateColumn(tableName: String, column: String, dataType: String): String {
        val sql = "ALTER TABLE $tableName ADD $column $dataType;"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLInsertTable(tableName: String, column: String, value: String): String {
        val sql = makeSQLInsertTable(tableName, column, value, true)
        Log.i("SQL ", sql)
        return sql
    }

    /**
     * @param tableName   table name
     * @param column      list column name
     * @param value       list corresponding column value
     * @param isMakeValue is make list value, default is true
     * @return sql excuse
     */
    fun makeSQLInsertTable(tableName: String, column: String, value: String, isMakeValue: Boolean): String {
        var vals = value
        if (isMakeValue) {
            vals = makeList(value)
        }
        val sql = "INSERT INTO $tableName ($column) VALUES ($vals);"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLInsertTable(tableName: String, columns: Array<String>, values: Array<String>): String {
        if (columns.size != values.size)
            throw RuntimeException("Number column and number value not match!")
        val cols = makeList(true, *columns)
        val vals = makeList(*values)
        val sql = "INSERT INTO $tableName ($cols) VALUES ($vals);"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLInsertTable(tableName: String, line: Line): String {
        val data = line.get()
        var cols = ""
        var vals = ""
        for (m in data) {
            if (cols == "") {
                cols += m.key?.let { makeList(true, it) }
            } else {
                cols = cols + "," + m.key?.let { makeList(true, it) }
            }
            if (vals == "") {
                vals += m.value?.let { makeList(it) }
            } else {
                vals = vals + "," + m.value?.let { makeList(it) }
            }
        }

        return makeSQLInsertTable(tableName, cols, vals, false)
    }

    fun makeSQLSelectTable(tableName: String): String {
        val sql = "SELECT * FROM $tableName;"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLSelectTable(tableName: String, condition: String): String {
        val sql: String
        if (condition == "") {
            sql = makeSQLSelectTable(tableName)
        } else {
            sql = "SELECT * FROM $tableName WHERE $condition;"
        }
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLSelectTable(tableName: String, column: String, condition: String): String {
        val sql: String
        if (condition == "") {
            sql = "SELECT $column FROM $tableName;"
        } else {
            sql = "SELECT $column FROM $tableName WHERE $condition;"
        }
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLAddColumn(tableName: String, newColumn: String, columnType: String): String {
        val sql = "ALTER TABLE $tableName ADD COLUMN $newColumn $columnType;"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLUpdate(tableName: String, columnsValues: String): String {
        val sql = "UPDATE $tableName SET $columnsValues;"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLUpdate(tableName: String, columnsValue: String, condition: String): String {
        val sql = "UPDATE $tableName SET $columnsValue WHERE $condition;"
        Log.i("SQL ", sql)
        return sql
    }

    fun makeSQLUpdate(tableName: String, line: Line, condition: String): String {
        val data = line.get()
        var list = ""
        for (m in data) {
            if (list == "") {
                list += makeList(true, makeCondition(m.key, m.value))
            } else {
                list = list + "," + makeList(true, makeCondition(m.key, m.value))
            }
        }

        val sql = makeSQLUpdate(tableName, list, condition)
        Log.i("SQL ", sql)
        return sql
    }

    fun makeCondition(column: String, value: Int): String {
        return makeCondition(column, value.toString())
    }

    @JvmOverloads
    fun makeCondition(column: String?, value: String?, isColumn: Boolean = false): String {
        if (isColumn) {
            return "$column=$value"
        } else {
            val sql: String
            return if (isNaN(value)) {
                sql = "$column='$value'"
                sql
            } else {
                sql = "$column=$value"
                sql
            }
        }
    }

    fun makeSum(column: String): String {
        return "SUM($column}"
    }

    fun makeLike(column: String, value: String): String {
        return "$column LIKE '%$value%'  COLLATE NOCASE"
    }


    fun makeMax(column: String): String {
        return "MAX($column)"
    }

    fun makeCount(column: String): String {
        return "COUNT($column)"
    }

    fun makeOr(vararg conditions: String): String {
        var sql = ""
        for (c in conditions) {
            sql = "$sql$c OR "
        }
        if (sql.endsWith(" OR ")) {
            sql = sql.substring(0, sql.lastIndexOf(" OR "))
        }
        return sql
    }

    fun makeAnd(vararg conditions: String): String {
        var sql = ""
        for (c in conditions) {
            sql = "$sql$c AND "
        }
        if (sql.endsWith(" AND ")) {
            sql = sql.substring(0, sql.lastIndexOf(" AND "))
        }
        return sql
    }

    fun exceptIDColumn(columns: Array<String>): Array<String> {
        return Arrays.copyOfRange(columns, 1, columns.size)
    }

    fun makeList(vararg args: String): String {
        return makeList(false, *args)
    }

    fun makeList(isColumn: Boolean, vararg args: String): String {
        var vals = ""
        for (s in args) {
            vals = if (isColumn) {
                "$vals$s,"
            } else {
                if (isNaN(s)) {
                    "$vals'$s',"
                } else {
                    "$vals$s,"
                }
            }
        }
        if (vals.endsWith(",")) vals = vals.substring(0, vals.lastIndexOf(","))
        return vals
    }

    fun isNaN(n: String?): Boolean {
        return !n!!.matches("-?\\d+(\\.\\d+)?".toRegex())
    }

    fun makePointTo(tableName: String, column: String): String {
        return "$tableName.$column"
    }

    fun makeSelectJoin(table1: String, table2: String, columns: String, condition: String, type: JoinType): String {
        var sql = ""
        when (type) {
            JoinType.LEFT, JoinType.RIGHT, JoinType.INNER -> sql = "SELECT $columns FROM $table1 $type JOIN $table2 ON $condition"
            JoinType.CROSS, JoinType.NATURAL -> {
                sql = "SELECT $columns FROM $table1 $type JOIN $table2;"
            }
            JoinType.OUTER -> sql = "SELECT $columns FROM $table1 FULL OUTER JOIN $table2 ON $condition"
        }
        return sql
    }

    enum class JoinType {
        INNER, LEFT, RIGHT, OUTER, NATURAL, CROSS

    }
}
