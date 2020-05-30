package com.example.Pharmacy;

import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.Pharmacy.test.data.AccountData;
import com.xu.database.Line;
import com.xu.database.TableManager;
import com.xu.database.XCursor;

import java.util.List;

public class TB_AccountManager extends TableManager {
    static final String TABLE_NAME = TableColumn.AccountTable.TABLE;
    private SQLiteDatabase database;

    TB_AccountManager(SQLiteDatabase database) {
        if (database == null) {
            throw new RuntimeException("Database is null!!!");
        } else {
            this.database = database;
        }
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void createTable() {
        //TODO: define table's column atlas
        String[] TABLE_COLUMNS_ATLAS = {
                TableColumn.AccountTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                TableColumn.AccountTable.USERNAME + " VARCHAR NOT NULL",
                TableColumn.AccountTable.PASSWORD + " VARCHAR NOT NULL"
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

        //TODO:  create default value for table
        if (!checkExited(database, TABLE_NAME, TableColumn.AccountTable.ID, "1")) {
            AccountData accountData = new AccountData();
            List<Line> data = accountData.getData();
            for (Line line : data) {
                database.execSQL(makeSQLInsertTable(TABLE_NAME, line));
            }
        }

        //TODO: show table
        XCursor cursor = selectTable();
        printData(TABLE_NAME, cursor);
        cursor.close();
    }

    @Override
    public void addColumn(String newColumn, String columnType) {
        database.execSQL(makeSQLAddColumn(TABLE_NAME, newColumn, columnType), null);
    }

    @Override
    public XCursor selectTable() {
        return new XCursor(database.rawQuery(makeSQLSelectTable(TABLE_NAME), null));
    }

    @Override
    public XCursor selectTable(String condition) {
        return new XCursor(database.rawQuery(makeSQLSelectTable(TABLE_NAME, condition), null));
    }

    @Override
    public XCursor selectTable(String column, String condition) {
        return new XCursor(database.rawQuery(makeSQLSelectTable(TABLE_NAME, column, condition), null));
    }

    @Override
    public void deleteTable() {
        deleteTable(database, TABLE_NAME);
    }

    /**
     * Check login
     *
     * @param user username
     * @param pass password
     *             call checkLogin to check result
     */
    boolean login(String user, String pass) {
        try {
            XCursor res = selectTable( TableColumn.AccountTable.PASSWORD, makeCondition(TableColumn.AccountTable.USERNAME, user));
            res.moveToFirst();
            if (res.getString(0).equals(pass)) {
                res.close();
                return true;
            } else {
                res.close();
                return false;
            }
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
            return false;
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
            return false;
        }
    }
}
