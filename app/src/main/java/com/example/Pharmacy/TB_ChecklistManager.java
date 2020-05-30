package com.example.Pharmacy;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.xu.database.Line;
import com.xu.database.TableManager;
import com.xu.database.XCursor;

class TB_ChecklistManager extends TableManager {

    static final String TABLE_NAME = TableColumn.ChecklistTable.TABLE;

    private SQLiteDatabase database;

    TB_ChecklistManager(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void createTable() {
        //TODO: define table's column atlas
        String[] TABLE_COLUMNS_ATLAS = {
                TableColumn.ChecklistTable.CID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                TableColumn.ChecklistTable.PID + " INTEGER NOT NULL",
                TableColumn.ChecklistTable.REAL + " INTEGER DEFAULT 0",
                TableColumn.ChecklistTable.UNIT_ID + " INTEGER",
                TableColumn.ChecklistTable.WAREHOUSE_ID + " INTEGER",
                TableColumn.ChecklistTable.QUEUE_ID + " INTEGER",
                TableColumn.ChecklistTable.CATEGORIES_ID + " INTEGER",
                TableColumn.ChecklistTable.DATE + " DATE",
                TableColumn.ChecklistTable.RECORDTIME + " DATE",
                TableColumn.ChecklistTable.CONFIRM + " INTEGER DEFAULT 0"
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

        addColumn(TableColumn.ChecklistTable.CONFIRM, " INTEGER DEFAULT 0");

        //TODO: show table
        XCursor cursor = selectTable();
        printData(TABLE_NAME, cursor);
        cursor.close();
    }

    @Override
    public void addColumn(String newColumn, String columnType) {
        try {
            database.execSQL(makeSQLAddColumn(TABLE_NAME, newColumn, columnType));
        } catch (SQLiteException ie) {
            ie.printStackTrace();
        }
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
     * getLine last id of list
     *
     * @param pid product id, protect data, make it's true
     * @return id of last list just insert
     */
    String getID(String pid) {
        try {
            Cursor cursor = selectTable(makeMax(TableColumn.ChecklistTable.CID), makeCondition(TableColumn.ChecklistTable.PID, pid));
            cursor.moveToFirst();
            String id = cursor.getString(0);
            cursor.close();
            return id;
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    void insertTable(Line line) {
        database.execSQL(makeSQLInsertTable(TABLE_NAME, line));
    }

    void updateTable(String cid, Line line) {
        database.execSQL(makeSQLUpdate(TABLE_NAME, line, makeCondition(TableColumn.ChecklistTable.CID, cid)));
    }

    void updateColumn(String cid, String column, String value){
        database.execSQL(makeSQLUpdate(TABLE_NAME, makeCondition(column, value), makeCondition(TableColumn.ChecklistTable.CID, cid)));
    }

    /**
     * @return tat ca cac phieu trong bang
     */
    XCursor getData() {
        return getData(null);
    }

    /**
     * @param cid so phieu
     * @return tat ca cac phieu co so phieu cid,
     * thuc te chi co duy nhat 1 phieu co so phieu cid
     */
    public XCursor getCard(String cid) {
        String cidCol = TableColumn.ChecklistTable.CID;
        return getData(makeAnd(
                makeCondition(makePointTo(TABLE_NAME, cidCol), cid)));
    }

    /**
     * @param specialCondition dieu kien dac biet,
     *                         dieu kien da duoc xu ly truoc
     * @return tat ca cac phieu thoa man dieu kien
     */
    XCursor getData(String specialCondition) {
        String cidCol = TableColumn.ChecklistTable.CID;
        String pidCol = TableColumn.ChecklistTable.PID;
        String realCol = TableColumn.ChecklistTable.REAL;
        String unitCol = TableColumn.ChecklistTable.UNIT_ID;
        String warehouseCol = TableColumn.ChecklistTable.WAREHOUSE_ID;
        String queueCol = TableColumn.ChecklistTable.QUEUE_ID;
        String categoriesCol = TableColumn.ChecklistTable.CATEGORIES_ID;
        String dateCol = TableColumn.ChecklistTable.DATE;
        String recordTimeCol = TableColumn.ChecklistTable.RECORDTIME;

        String listTable = makeList(true,
                TB_ChecklistManager.TABLE_NAME,
                TB_ProductInStockManager.TABLE_NAME,
                TB_ProductManager.TABLE_NAME,
                TB_WarehouseManager.TABLE_NAME,
                TB_CategoriesManager.TABLE_NAME,
                TB_QueueManager.TABLE_NAME
        );
        String listColumn = makeList(true,
                makePointTo(TB_ChecklistManager.TABLE_NAME, cidCol),
                makePointTo(TB_ChecklistManager.TABLE_NAME, pidCol),
                makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME),
                makePointTo(TB_ChecklistManager.TABLE_NAME, realCol),
                makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.TOTAL),
                makePointTo(TB_ChecklistManager.TABLE_NAME, TableColumn.ChecklistTable.UNIT_ID),
                makePointTo(TB_QueueManager.TABLE_NAME, TableColumn.QueueTable.QUEUE),
                makePointTo(TB_WarehouseManager.TABLE_NAME, TableColumn.WarehouseTable.WAREHOUSE),
                makePointTo(TB_CategoriesManager.TABLE_NAME, TableColumn.CategoriesTable.CATEGORIES),
                makePointTo(TB_ChecklistManager.TABLE_NAME, dateCol),
                makePointTo(TB_ChecklistManager.TABLE_NAME, recordTimeCol),
                makePointTo(TB_ChecklistManager.TABLE_NAME, TableColumn.ChecklistTable.CONFIRM),
                makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.PRICE));

        String listCondition = makeAnd(
                makeCondition(makePointTo(TABLE_NAME, pidCol), makePointTo(TB_ProductInStockManager.TABLE_NAME, pidCol), true),
                makeCondition(makePointTo(TABLE_NAME, pidCol), makePointTo(TB_ProductManager.TABLE_NAME, pidCol), true),
                makeCondition(makePointTo(TABLE_NAME, categoriesCol), makePointTo(TB_CategoriesManager.TABLE_NAME, categoriesCol), true),
                makeCondition(makePointTo(TABLE_NAME, warehouseCol), makePointTo(TB_WarehouseManager.TABLE_NAME, warehouseCol), true),
                makeCondition(makePointTo(TABLE_NAME, queueCol), makePointTo(TB_QueueManager.TABLE_NAME, queueCol), true),
                //make link to product in stock table to getLine total line
                makeCondition(makePointTo(TABLE_NAME, unitCol), makePointTo(TB_ProductInStockManager.TABLE_NAME, unitCol), true),
                makeCondition(makePointTo(TABLE_NAME, pidCol), makePointTo(TB_ProductInStockManager.TABLE_NAME, pidCol), true),
                makeCondition(makePointTo(TABLE_NAME, categoriesCol), makePointTo(TB_ProductInStockManager.TABLE_NAME, categoriesCol), true),
                makeCondition(makePointTo(TABLE_NAME, warehouseCol), makePointTo(TB_ProductInStockManager.TABLE_NAME, warehouseCol), true),
                makeCondition(makePointTo(TABLE_NAME, queueCol), makePointTo(TB_ProductInStockManager.TABLE_NAME, queueCol), true));

        if (specialCondition != null) {
            listCondition = makeAnd(listCondition, specialCondition);
        }

        XCursor cursor = new XCursor(database.rawQuery(makeSQLSelectTable(listTable, listColumn, listCondition), null));
        printData(TABLE_NAME + specialCondition, cursor);
        return cursor;
    }

    /**
     * @return tat ca cac phieu bi trung
     * phieu trung la phieu cung 1 san pham, 1 kho, 1 loai hang, 1 hang cho, 1 date
     */
    XCursor getData(String pid, String unit, String warehouse, String catogories, String queue, String date) {
        return getData(makeAnd(
                makeCondition(makePointTo(TABLE_NAME, TableColumn.ChecklistTable.PID), pid),
                makeCondition(makePointTo(TABLE_NAME, TableColumn.ChecklistTable.UNIT_ID), unit),
                makeCondition(makePointTo(TABLE_NAME, TableColumn.ChecklistTable.WAREHOUSE_ID), warehouse),
                makeCondition(makePointTo(TABLE_NAME, TableColumn.ChecklistTable.CATEGORIES_ID), catogories),
                makeCondition(makePointTo(TABLE_NAME, TableColumn.ChecklistTable.QUEUE_ID), queue),
                makeCondition(makePointTo(TABLE_NAME, TableColumn.ChecklistTable.DATE), date)));
    }
}
