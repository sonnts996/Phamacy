package com.example.Pharmacy;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.Pharmacy.test.data.ProductData;
import com.xu.database.Line;
import com.xu.database.TableReadOnlyManager;
import com.xu.database.XCursor;

import java.util.List;

public class TB_ProductManager extends TableReadOnlyManager {

    static final String TABLE_NAME = TableColumn.ProductTable.TABLE;
    private SQLiteDatabase database;

    TB_ProductManager(SQLiteDatabase database) {
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
        final String[] TABLE_COLUMNS_ATLAS = {
                TableColumn.ProductTable.ID + " INTEGER PRIMARY KEY",
                TableColumn.ProductTable.NAME + " VARCHAR NOT NULL",
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

        //TODO: create default value for table
        if (!checkExited(database, TABLE_NAME, TableColumn.ProductTable.ID, "10150")) {
            try {
                ProductData productData = new ProductData();
                List<Line> data = productData.getData();
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
            Cursor cursor = selectTable(TableColumn.ProductTable.ID, makeCondition(TableColumn.ProductTable.NAME, name));
            String id = "";
            if (cursor != null) {
                cursor.moveToFirst();
                id = cursor.getString(0);
                cursor.close();
            }
            return id;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return "";
    }

    @Override
    public String getName(String id) {
        try {
            Cursor cursor = selectTable(TableColumn.ProductTable.NAME, makeCondition(TableColumn.ProductTable.ID, id));
            String name = "";
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(0);
                if (name.equals("")) name = "";
            }
            cursor.close();
            return name;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean isExist(String name) {
        return checkExited(database, TABLE_NAME, TableColumn.ProductTable.NAME, name);
    }
}
