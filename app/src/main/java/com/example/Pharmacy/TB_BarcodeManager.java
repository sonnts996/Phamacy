package com.example.Pharmacy;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.Pharmacy.test.data.BarcodeData;
import com.xu.database.Line;
import com.xu.database.TableReadOnlyManager;
import com.xu.database.XCursor;

import java.util.List;

public class TB_BarcodeManager extends TableReadOnlyManager {
    static final String TABLE_NAME = TableColumn.BarCodeTable.TABLE;
    private SQLiteDatabase database;

    TB_BarcodeManager(SQLiteDatabase database) {
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
                TableColumn.BarCodeTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                TableColumn.BarCodeTable.BARCODE + " VARCHAR",
                TableColumn.BarCodeTable.PID + " INTEGER NOT NULL"
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

        //TODO: create default value for table
        if (!checkExited(database, TABLE_NAME, TableColumn.BarCodeTable.ID, "1")) {
            BarcodeData barcodeData = new BarcodeData();
            List<Line> data = barcodeData.getData();
            for (Line line : data) {
                database.execSQL(makeSQLInsertTable(TABLE_NAME, line));
            }
        }

        //TODO: show table
        XCursor xCursor = selectTable();
        printData(TABLE_NAME, xCursor);
        xCursor.close();
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
     * Su dung barcode de tra ma san pham tuong tung
     * > class khong su dung den barcode ID
     *
     * @param name ma barcode
     * @return ma san pham
     */
    @Override
    public String getID(String name) {
        try {
            Cursor cursor = selectTable(TableColumn.BarCodeTable.PID, makeCondition(TableColumn.BarCodeTable.BARCODE, name));
            String id = "";
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(0);
                if (id.equals("")) id = "";
            }
            cursor.close();
            return id;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return null;
    }

    /**
     * Su dung ma san pham de tim barcode tuong ung
     * > class khong su dung den barcode id
     *
     * @param id ma san pham
     * @return barcode duoi dang chuoi "string"
     */
    @Override
    public String getName(String id) {
        try {
            Cursor cursor = selectTable(TableColumn.BarCodeTable.BARCODE, makeCondition(TableColumn.BarCodeTable.PID, id));
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
        return checkExited(database, TABLE_NAME, TableColumn.BarCodeTable.BARCODE, name);
    }

}
