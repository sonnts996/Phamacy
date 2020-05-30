package com.example.Pharmacy;

import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.Pharmacy.test.data.UnitData;
import com.xu.database.Line;
import com.xu.database.TableReadOnlyManager;
import com.xu.database.XCursor;

import java.util.List;

public class TB_UnitManager extends TableReadOnlyManager {

    static final String TABLE_NAME = TableColumn.UnitTable.TABLE;
    private SQLiteDatabase database;

    TB_UnitManager(SQLiteDatabase database) {
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
                TableColumn.UnitTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                TableColumn.UnitTable.UNIT + " VARCHAR",
                TableColumn.UnitTable.UNIT2 + " VARCHAR",
                TableColumn.UnitTable.FACTOR2 + " INTEGER",
                TableColumn.UnitTable.UNIT3 + " VARCHAR",
                TableColumn.UnitTable.FACTOR3 + " INTEGER"
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

        //TODO: create default value for table
        if (!checkExited(database, TABLE_NAME, TableColumn.UnitTable.ID, "1")) {
            try {
                UnitData unitData = new UnitData();
                List<Line> data = unitData.getData();
                for (Line line : data) {
                    database.execSQL(makeSQLInsertTable(TABLE_NAME, line));
                }
            } catch (SQLiteException sql_ex) {
                sql_ex.printStackTrace();
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

    @Override
    public String getID(String name) {
        try {
            XCursor cursor = selectTable(TableColumn.UnitTable.ID, makeCondition(TableColumn.UnitTable.UNIT, name));
            cursor.moveToFirst();
            String id = cursor.getString(0);
            cursor.close();
            return id;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName(String id) {
        try {
            XCursor cursor = selectTable(TableColumn.UnitTable.UNIT, makeCondition(TableColumn.UnitTable.ID, id));
            cursor.moveToFirst();
            String name = cursor.getString(0);
            cursor.close();
            return name;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isExist(String name) {
        return checkExited(database, TABLE_NAME, TableColumn.UnitTable.UNIT, name);
    }
}
