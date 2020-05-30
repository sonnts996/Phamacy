package com.example.Pharmacy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.Pharmacy.test.data.ProductInStockData;
import com.xu.database.Line;
import com.xu.database.TableManager;
import com.xu.database.XCursor;

import java.util.List;

public class TB_ProductInStockManager extends TableManager {
    public static final String[] TABLE_COLUMNS = {
            TableColumn.ProductInStockTable.STOCK_ID,
            TableColumn.ProductInStockTable.PID,
            TableColumn.ProductInStockTable.TOTAL, //must be factor1
            TableColumn.ProductInStockTable.UNIT_ID,
            TableColumn.ProductInStockTable.WAREHOUSE_ID,
            TableColumn.ProductInStockTable.QUEUE_ID,
            TableColumn.ProductInStockTable.CATEGORIES_ID,
            TableColumn.ProductInStockTable.DATE
    };
    static final String TABLE_NAME = TableColumn.ProductInStockTable.TABLE;
    private SQLiteDatabase database;

    TB_ProductInStockManager(SQLiteDatabase database) {
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
                TableColumn.ProductInStockTable.STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                TableColumn.ProductInStockTable.PID + " INTEGER NOT NULL",
                TableColumn.ProductInStockTable.TOTAL + " INTEGER DEFAULT 0",
                TableColumn.ProductInStockTable.UNIT_ID + " INTEGER",
                TableColumn.ProductInStockTable.WAREHOUSE_ID + " INTEGER",
                TableColumn.ProductInStockTable.QUEUE_ID + " INTEGER",
                TableColumn.ProductInStockTable.CATEGORIES_ID + " INTEGER",
                TableColumn.ProductInStockTable.DATE + " DATE",
                TableColumn.ProductInStockTable.PRICE + " INTEGER DEFAULT 0"
        };

        //TODO: create table
        database.execSQL(makeSQLCreateTable(TABLE_NAME, TABLE_COLUMNS_ATLAS));

        //TODO: create default value for table
        if (!checkExited(database, TABLE_NAME, TableColumn.ProductInStockTable.STOCK_ID, "1")) {
            try {
                ProductInStockData productInStockData = new ProductInStockData();
                List<Line> data = productInStockData.getData();
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


    public XCursor getData(String pid) {
        String pidCol = TableColumn.ProductInStockTable.PID;
        return searchData(makeCondition(makePointTo(TABLE_NAME, pidCol), pid));
    }

    /**
     * @param specialCondition dieu kien dac biet,
     *                         dieu kien da duoc xu ly truoc
     * @return tat ca cac phieu thoa man dieu kien
     */
    XCursor searchData(String specialCondition) {
        String pidCol = TableColumn.ProductInStockTable.PID;
        String totalCol = TableColumn.ProductInStockTable.TOTAL;
        String unitCol = TableColumn.ProductInStockTable.UNIT_ID;
        String warehouseCol = TableColumn.ProductInStockTable.WAREHOUSE_ID;
        String categoriesCol = TableColumn.ProductInStockTable.CATEGORIES_ID;
        String queueCol = TableColumn.ProductInStockTable.QUEUE_ID;
        String listTable = makeList(true,
                TB_ProductInStockManager.TABLE_NAME,
                TB_ProductManager.TABLE_NAME,
                TB_UnitManager.TABLE_NAME,
                TB_WarehouseManager.TABLE_NAME,
                TB_CategoriesManager.TABLE_NAME,
                TB_QueueManager.TABLE_NAME
        );
        String listColumn = makeList(true,
                makePointTo(TB_ProductInStockManager.TABLE_NAME, pidCol),
                makePointTo(TB_ProductInStockManager.TABLE_NAME, totalCol),
                makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME),
                makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.UNIT_ID),
                makePointTo(TB_QueueManager.TABLE_NAME, TableColumn.QueueTable.QUEUE),
                makePointTo(TB_WarehouseManager.TABLE_NAME, TableColumn.WarehouseTable.WAREHOUSE),
                makePointTo(TB_CategoriesManager.TABLE_NAME, TableColumn.CategoriesTable.CATEGORIES),
                makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.STOCK_ID));

        String listCondition = makeAnd(
                makeCondition(makePointTo(TABLE_NAME, unitCol), makePointTo(TB_UnitManager.TABLE_NAME, unitCol), true),
                makeCondition(makePointTo(TABLE_NAME, pidCol), makePointTo(TB_ProductManager.TABLE_NAME, pidCol), true),
                makeCondition(makePointTo(TABLE_NAME, categoriesCol), makePointTo(TB_CategoriesManager.TABLE_NAME, categoriesCol), true),
                makeCondition(makePointTo(TABLE_NAME, warehouseCol), makePointTo(TB_WarehouseManager.TABLE_NAME, warehouseCol), true),
                makeCondition(makePointTo(TABLE_NAME, queueCol), makePointTo(TB_QueueManager.TABLE_NAME, queueCol), true));

        if (specialCondition != null) {
            listCondition = makeAnd(listCondition, specialCondition);
        }

        XCursor cursor = new XCursor(database.rawQuery(makeSQLSelectTable(listTable, listColumn, listCondition), null));
        printData("Product Data of " + specialCondition, cursor);
        return cursor;
    }

    XCursor getData() {
        return searchData(null);
    }
}
