package com.example.Pharmacy;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.xu.database.Line;
import com.xu.database.TableManager;
import com.xu.database.XCursor;

public class TB_HistoriesChecklistManager extends TableManager {
    static final String TABLE_NAME = TableColumn.HistoriesChecklistTable.TABLE;

    SQLiteDatabase database;

    TB_HistoriesChecklistManager(SQLiteDatabase database) {
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
                TableColumn.HistoriesChecklistTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                TableColumn.HistoriesChecklistTable.CID + " INTEGER NOT NULL",
                TableColumn.HistoriesChecklistTable.PID + " INTEGER NOT NULL",
                TableColumn.HistoriesChecklistTable.REAL + " INTEGER DEFAULT 0",
                TableColumn.HistoriesChecklistTable.UNIT_ID + " INTEGER",
                TableColumn.HistoriesChecklistTable.WAREHOUSE_ID + " INTEGER",
                TableColumn.HistoriesChecklistTable.QUEUE_ID + " INTEGER",
                TableColumn.HistoriesChecklistTable.CATEGORIES_ID + " INTEGER",
                TableColumn.HistoriesChecklistTable.DATE + " DATE",
                TableColumn.HistoriesChecklistTable.SYNCCOUNT + " INTEGER",
                TableColumn.HistoriesChecklistTable.SYNCTIME + " DATE",
                TableColumn.HistoriesChecklistTable.SYNCACCOUNT + " INTEGER",
                TableColumn.HistoriesChecklistTable.BARCODE + " VARCHAR"
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

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
     * Convert CID to this Table ID
     *
     * @param cid card ID
     * @return this table ID
     */
    String getID(String cid) {
        try {
            Cursor cursor = database.rawQuery(makeSQLSelectTable(TABLE_NAME, makeMax(TableColumn.HistoriesChecklistTable.ID), makeCondition(TableColumn.HistoriesChecklistTable.CID, cid)), null);
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

    String getName(String id) {
        try {
            String pid = this.getPID(id);
            TB_ProductManager tbProductManager = new TB_ProductManager(database);
            return tbProductManager.getName(pid);
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return null;
    }

    /**
     * convert CID to PID
     *
     * @param cid Card ID
     * @return Product ID
     */
    String getPID(String cid) {
        try {
            Cursor cursor = database.rawQuery(makeSQLSelectTable(TABLE_NAME, TableColumn.HistoriesChecklistTable.PID, makeCondition(TableColumn.HistoriesChecklistTable.CID, cid)), null);
            String id = "";
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(0);
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

    int getSyncCount(String cid) {
        try {
            Cursor cursor = selectTable(makeCount(TableColumn.HistoriesChecklistTable.CID), makeCondition(TableColumn.HistoriesChecklistTable.CID, cid));
            int count = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
            return count;
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            cur_ex.printStackTrace();
        }
        return 0;
    }

    void insertTable(Line line) {
        database.execSQL(makeSQLInsertTable(TABLE_NAME, line));
    }

    void updateSyncCount(String cid, String count) {
        database.execSQL(makeSQLUpdate(TABLE_NAME, makeCondition(TableColumn.HistoriesChecklistTable.SYNCCOUNT, count), makeCondition(TableColumn.HistoriesChecklistTable.CID, cid)));
    }

    public XCursor getData() {
        return getData(null);
    }

    /**
     * @param specialCondition is and condition(s) for filter data, it is a rule not a key work
     * @return cursor data
     */
    public XCursor getData(String specialCondition) {
        String cidCol = TableColumn.HistoriesChecklistTable.CID;
        String pidCol = TableColumn.HistoriesChecklistTable.PID;
        String realCol = TableColumn.HistoriesChecklistTable.REAL;
        String unitCol = TableColumn.HistoriesChecklistTable.UNIT_ID;
        String barcodeCol = TableColumn.HistoriesChecklistTable.BARCODE;
        String warehouseCol = TableColumn.HistoriesChecklistTable.WAREHOUSE_ID;
        String queueCol = TableColumn.HistoriesChecklistTable.QUEUE_ID;
        String categoriesCol = TableColumn.HistoriesChecklistTable.CATEGORIES_ID;
        String dateCol = TableColumn.HistoriesChecklistTable.DATE;
        String syncAccountCol = TableColumn.HistoriesChecklistTable.SYNCACCOUNT;
        String syncCountCol = TableColumn.HistoriesChecklistTable.SYNCCOUNT;

        String listTable = makeList(true,
                TB_HistoriesChecklistManager.TABLE_NAME,
                TB_ProductManager.TABLE_NAME,
                TB_WarehouseManager.TABLE_NAME,
                TB_CategoriesManager.TABLE_NAME,
                TB_QueueManager.TABLE_NAME,
                TB_UnitManager.TABLE_NAME
        );
        String listColumn = makeList(true,
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, cidCol),
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, pidCol),
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, barcodeCol),
                makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME),
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, realCol),
                makePointTo(TB_UnitManager.TABLE_NAME, TableColumn.UnitTable.UNIT),
                makePointTo(TB_QueueManager.TABLE_NAME, TableColumn.QueueTable.QUEUE),
                makePointTo(TB_WarehouseManager.TABLE_NAME, TableColumn.WarehouseTable.WAREHOUSE),
                makePointTo(TB_CategoriesManager.TABLE_NAME, TableColumn.CategoriesTable.CATEGORIES),
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, dateCol),
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, syncAccountCol),
                makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, syncCountCol));

        String listCondition = makeAnd(
                makeCondition(makePointTo(TABLE_NAME, pidCol), makePointTo(TB_ProductManager.TABLE_NAME, pidCol), true),
                makeCondition(makePointTo(TABLE_NAME, categoriesCol), makePointTo(TB_CategoriesManager.TABLE_NAME, categoriesCol), true),
                makeCondition(makePointTo(TABLE_NAME, warehouseCol), makePointTo(TB_WarehouseManager.TABLE_NAME, warehouseCol), true),
                makeCondition(makePointTo(TABLE_NAME, queueCol), makePointTo(TB_QueueManager.TABLE_NAME, queueCol), true),
                makeCondition(makePointTo(TABLE_NAME, unitCol), makePointTo(TB_UnitManager.TABLE_NAME, TableColumn.UnitTable.ID),true));

        if (specialCondition != null) {
            listCondition = makeAnd (listCondition, specialCondition);
        }

        XCursor cursor = new XCursor(database.rawQuery(makeSQLSelectTable(listTable, listColumn, listCondition), null));
        printData(TABLE_NAME + specialCondition, cursor);
        return cursor;
    }
}
