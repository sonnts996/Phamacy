package com.example.Pharmacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.xu.database.Line;
import com.xu.database.MapData;
import com.xu.database.SQLMaker;
import com.xu.database.XCursor;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class DataManager {
    private Context appContext;
    private SQLiteDatabase database;
    private boolean isLogin = false;
    private String username = "";
    private String rootFolder = "";

    private SQLMaker SQL = new SQLMaker();
    private TB_AccountManager tbAccountManager;
    private TB_ProductManager tbProductManager;
    private TB_ChecklistManager tbChecklistManager;
    private TB_HistoriesChecklistManager tbHistoriesChecklistTable;
    private TB_BarcodeManager tbBarcodeManager;
    private TB_QueueManager tbQueueManager;
    private TB_WarehouseManager tbWarehouseManager;
    private TB_UnitManager tbUnitManager;
    private TB_CategoriesManager tbCategoriesManager;
    private TB_ProductInStockManager tbProductInStockManager;

    DataManager(Context context) throws RuntimeException {
        setAppContext(context);

        if (!createDatabase()) {
            throw new RuntimeException("Can not create Database");
        } else {
            if (!createTable()) {
                throw new RuntimeException("Can not create User Account");
            }
        }
    }

    private void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    private boolean isLogin() {
        return isLogin;
    }

    private void setLogin(boolean login) {
        isLogin = login;
    }

    String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private String getRootFolder() {
        return rootFolder;
    }

    private void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }


    /**
     * check and create if not exists folder contain database
     *
     * @return is existed
     */
    private boolean createRoot() {
        String path = appContext.getFilesDir().toString() + File.separator + appContext.getPackageName();
        File root = new File(path);
        if (!root.exists()) {
            Log.i("CREATE ROOT", "Database was not exist!");
            if (root.mkdirs()) {
                setRootFolder(path);
                Log.i("CREATE ROOT", "Create Folder" + getRootFolder());
            } else {
                Log.e("CREATE ROOT", "Create Folder failed! " + path);
                return false;
            }
        } else {
            setRootFolder(path);
        }
        return root.exists();
    }

    /**
     * create or open database
     *
     * @return is success
     */
    private boolean createDatabase() {
        try {
            if (createRoot()) {
                database = SQLiteDatabase.openOrCreateDatabase(getRootFolder() + "/data", null);
                Log.i("DATAMNG", getRootFolder());
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * close database
     */
    void close() {
        try {
            database.close();
        } catch (SQLiteException sql_ex) {
            sql_ex.printStackTrace();
        }
    }


    /**
     * after create database
     *
     * @return success
     */
    private boolean createTable() {
        tbAccountManager = new TB_AccountManager(database);
        tbProductManager = new TB_ProductManager(database);
        tbChecklistManager = new TB_ChecklistManager(database);
        tbHistoriesChecklistTable = new TB_HistoriesChecklistManager(database);
        tbBarcodeManager = new TB_BarcodeManager(database);
        tbQueueManager = new TB_QueueManager(database);
        tbUnitManager = new TB_UnitManager(database);
        tbWarehouseManager = new TB_WarehouseManager(database);
        tbCategoriesManager = new TB_CategoriesManager(database);
        tbProductInStockManager = new TB_ProductInStockManager(database);

//        tbAccountManager.deleteTable();
//        tbProductInStockManager.deleteTable();
//        tbBarcodeManager.deleteTable();
//        tbQueueManager.deleteTable();
//        tbUnitManager.deleteTable();
//        tbCategoriesManager.deleteTable();
//        tbProductManager.deleteTable();
//        tbChecklistManager.deleteTable();
//        tbHistoriesChecklistTable.deleteTable();
//        tbWarehouseManager.deleteTable();

        try {
            create_userTable();
            create_productTable();
            create_checkListTable();
            create_historiesCheckListTable();
            create_queueTable();
            create_barcodeTable();
            create_unitTable();
            create_warehouseTable();
            create_categoriesTable();
            create_productInStockManager();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void create_userTable() {
        tbAccountManager.createTable();
    }

    private void create_productTable() {
        tbProductManager.createTable();
    }

    private void create_warehouseTable() {
        tbWarehouseManager.createTable();
    }

    private void create_unitTable() {
        tbUnitManager.createTable();
    }

    private void create_categoriesTable() {
        tbCategoriesManager.createTable();
    }

    private void create_queueTable() {
        tbQueueManager.createTable();
    }

    private void create_barcodeTable() {
        tbBarcodeManager.createTable();
    }

    private void create_checkListTable() {
        tbChecklistManager.createTable();
    }

    private void create_historiesCheckListTable() {
        tbHistoriesChecklistTable.createTable();
    }

    private void create_productInStockManager() {
        tbProductInStockManager.createTable();
    }


    /**
     * Check login
     * call checkLogin to check result
     *
     * @param user username
     * @param pass password
     */
    void login(String user, String pass) {
        if (tbAccountManager.login(user, pass)) {
            setUsername(user);
            setLogin(true);
        } else {
            setLogin(false);
        }
    }

    /**
     * check login stage
     *
     * @return is login
     */
    boolean checkLogin() {
        return isLogin();
    }

    boolean insertData(String pid, String barcode, String real, String unit, String warehouse, String isqueue, String categories, String date) {
        try {
            addChecklist(pid, real, unit, warehouse, isqueue, categories, date);
            addHistory(pid, barcode, real, unit, warehouse, isqueue, categories, date);
            SQL.printData(TB_ChecklistManager.TABLE_NAME, tbChecklistManager.selectTable());
            SQL.printData(TB_HistoriesChecklistManager.TABLE_NAME, tbHistoriesChecklistTable.selectTable());
            return true;
        } catch (SQLiteException sql_ex) {
            Toast.makeText(appContext, "Lỗi!!! Không thể lưu dữ liệu.", Toast.LENGTH_LONG).show();
            sql_ex.printStackTrace();
            return false;
        } catch (RuntimeException re) {
            re.printStackTrace();
            return false;
        }
    }

    boolean updateData(String cid, String pid, String barcode, String real, String unit, String warehouse, String isqueue, String categories, String date) {
        try {
            updateChecklist(cid, pid, real, unit, warehouse, isqueue, categories, date);
            addHistory(pid, barcode, real, unit, warehouse, isqueue, categories, date);
            SQL.printData(TB_ChecklistManager.TABLE_NAME, tbChecklistManager.selectTable());
            SQL.printData(TB_HistoriesChecklistManager.TABLE_NAME, tbHistoriesChecklistTable.selectTable());
            return true;
        } catch (SQLiteException sql_ex) {
            Toast.makeText(appContext, "Lỗi!!! Không thể lưu dữ liệu.", Toast.LENGTH_LONG).show();
            Log.e("SQL", sql_ex.toString());
            return false;
        }
    }

    private void addChecklist(String pid, String real, String unit, String warehouse, String isqueue, String categories, String date) {
        String pidCol = TableColumn.ChecklistTable.PID;
        String realCol = TableColumn.ChecklistTable.REAL;
        String unitCol = TableColumn.ChecklistTable.UNIT_ID;
        String warehouseCol = TableColumn.ChecklistTable.WAREHOUSE_ID;
        String queueCol = TableColumn.ChecklistTable.QUEUE_ID;
        String categoriesCol = TableColumn.ChecklistTable.CATEGORIES_ID;
        String dateCol = TableColumn.ChecklistTable.DATE;
        String recordTime = TableColumn.ChecklistTable.RECORDTIME;

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d = new Date();

        Line line = new Line(
                new MapData(pidCol, pid),
                new MapData(realCol, real),
                new MapData(unitCol, unit),
                new MapData(warehouseCol, tbWarehouseManager.getID(warehouse)),
                new MapData(queueCol, tbQueueManager.getID(isqueue)),
                new MapData(categoriesCol, tbCategoriesManager.getID(categories)),
                new MapData(dateCol, date),
                new MapData(recordTime, dateFormat.format(d)));

        tbChecklistManager.insertTable(line);
    }

    private void updateChecklist(String cid, String pid, String real, String unit, String warehouse, String isqueue, String categories, String date) {
        String pidCol = TableColumn.ChecklistTable.PID;
        String realCol = TableColumn.ChecklistTable.REAL;
        String unitCol = TableColumn.ChecklistTable.UNIT_ID;
        String warehouseCol = TableColumn.ChecklistTable.WAREHOUSE_ID;
        String queueCol = TableColumn.ChecklistTable.QUEUE_ID;
        String categoriesCol = TableColumn.ChecklistTable.CATEGORIES_ID;
        String dateCol = TableColumn.ChecklistTable.DATE;
        String recordTime = TableColumn.ChecklistTable.RECORDTIME;

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d = new Date();

        Line line = new Line(
                new MapData(pidCol, pid),
                new MapData(realCol, real),
                new MapData(unitCol, unit),
                new MapData(warehouseCol, tbWarehouseManager.getID(warehouse)),
                new MapData(queueCol, tbQueueManager.getID(isqueue)),
                new MapData(categoriesCol, tbCategoriesManager.getID(categories)),
                new MapData(dateCol, date),
                new MapData(recordTime, dateFormat.format(d)));

        tbChecklistManager.updateTable(cid, line);
    }

    private void addHistory(String pid, String barcode, String real, String unit, String warehouse, String isqueue, String categories, String date) {
        String cid = tbChecklistManager.getID(pid);
        if (cid.equals("")) {
            Toast.makeText(appContext, "Lỗi!!! Không thể lưu dữ liệu.", Toast.LENGTH_LONG).show();
            throw new RuntimeException("CID is null");
        } else {
            int count = tbHistoriesChecklistTable.getSyncCount(cid);
            count++;
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d = new Date();

            String cidCol = TableColumn.HistoriesChecklistTable.CID;
            String pidCol = TableColumn.HistoriesChecklistTable.PID;
            String barcodeCol = TableColumn.HistoriesChecklistTable.BARCODE;
            String realCol = TableColumn.HistoriesChecklistTable.REAL;
            String unitCol = TableColumn.HistoriesChecklistTable.UNIT_ID;
            String warehouseCol = TableColumn.HistoriesChecklistTable.WAREHOUSE_ID;
            String queueCol = TableColumn.HistoriesChecklistTable.QUEUE_ID;
            String categoriesCol = TableColumn.HistoriesChecklistTable.CATEGORIES_ID;
            String dateCol = TableColumn.HistoriesChecklistTable.DATE;
            String syncAccountCol = TableColumn.HistoriesChecklistTable.SYNCACCOUNT;
            String syncTimeCol = TableColumn.HistoriesChecklistTable.SYNCTIME;
            String syncCountCol = TableColumn.HistoriesChecklistTable.SYNCCOUNT;

            Line line = new Line(
                    new MapData(cidCol, cid),
                    new MapData(pidCol, pid),
                    new MapData(barcodeCol, barcode),
                    new MapData(realCol, real),
                    new MapData(unitCol, unit),
                    new MapData(warehouseCol, tbWarehouseManager.getID(warehouse)),
                    new MapData(queueCol, tbQueueManager.getID(isqueue)),
                    new MapData(categoriesCol, tbCategoriesManager.getID(categories)),
                    new MapData(dateCol, date),
                    new MapData(syncAccountCol, getUsername()),
                    new MapData(syncCountCol, count),
                    new MapData(syncTimeCol, dateFormat.format(d))
            );

            tbHistoriesChecklistTable.insertTable(line);
            tbHistoriesChecklistTable.updateSyncCount(cid, String.valueOf(count));
        }
    }


    XCursor getChecklistCard(int cid) {
        try {
            XCursor res = tbChecklistManager.getCard(String.valueOf(cid));
            if (res.getCount() <= 0) return null;
            else return res;
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor getChecklistCard() {
        try {
            return tbChecklistManager.getData();
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor searchChecklistCard(String value) {
        try {
            XCursor cursor;
            if (value.equals("") || value.equals("*")) {
                cursor = tbChecklistManager.getData();
            } else {
                cursor = tbChecklistManager.getData("(" +
                        SQL.makeCondition(SQL.makePointTo(TB_ChecklistManager.TABLE_NAME, TableColumn.ChecklistTable.PID), value).concat(" OR ") +
                        SQL.makeLike(SQL.makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME), value) +
                        ")");
            }
            return cursor;
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor getChecklistCardByDate(String date) {
        try {
            return tbChecklistManager.getData(SQL.makeCondition(SQL.makePointTo(TB_ChecklistManager.TABLE_NAME, TableColumn.ChecklistTable.RECORDTIME), date));
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor getChecklistCard(String name, String unit_id, String warehouse, String catogories, String queue, String date) {
        String pid = tbProductManager.getID(name);
        String warehouse_id = tbWarehouseManager.getID(warehouse);
        String categories_id = tbCategoriesManager.getID(catogories);
        String queue_id = tbQueueManager.getID(queue);
        return tbChecklistManager.getData(pid, unit_id, warehouse_id, categories_id, queue_id, date);
    }

    XCursor getHistories() {
        try {
            return tbHistoriesChecklistTable.getData();
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor getHistories(String date) {
        try {
            return tbHistoriesChecklistTable.getData(SQL.makeCondition(SQL.makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, TableColumn.HistoriesChecklistTable.SYNCTIME), date));
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor getHistoriesByName(String value) {
        try {
            XCursor cursor;
            if (value.equals("") || value.toLowerCase().equals("*")) {
                cursor = tbHistoriesChecklistTable.getData();
            } else {
                cursor = tbHistoriesChecklistTable.getData("(" +
                        SQL.makeCondition(SQL.makePointTo(TB_HistoriesChecklistManager.TABLE_NAME, TableColumn.HistoriesChecklistTable.PID), value).concat(" OR ") +
                        SQL.makeLike(SQL.makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME), value) +
                        ")");
            }
            return cursor;
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }


    XCursor getProductData(String value) {
        String pid = tbProductManager.getID(value);
        return tbProductInStockManager.getData(pid);
    }

    String getBarCodeProduct(String code) {
        String pid = tbBarcodeManager.getID(code);
        return tbProductManager.getName(pid);
    }

    XCursor searchProductData(String value) {
        try {
            XCursor cursor;
            if (value.equals("") || value.equals("*")) {
                cursor = tbProductInStockManager.getData();
            } else {
                cursor = tbProductInStockManager.searchData("(" +
                        SQL.makeCondition(SQL.makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.PID), value).concat(" OR ") +
                        SQL.makeLike(SQL.makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME), value) +
                        ")");
            }
            return cursor;
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    String getPID(String name) {
        return tbProductManager.getID(name);
    }

    String getName(String pid) {
        return tbProductManager.getName(pid);
    }

    XCursor getNameList() {
        try {
            return tbProductManager.selectTable(TableColumn.ProductTable.NAME, "");
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    XCursor getUnit(String unit_id) {
        return tbUnitManager.selectTable(SQL.makeCondition(TableColumn.UnitTable.ID, unit_id));
    }

    XCursor getUnitInStock(String name, String warehouse) {
        String pid = tbProductManager.getID(name);
        return tbProductInStockManager.searchData(SQL.makeAnd(
                SQL.makeCondition(SQL.makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.PID), pid),
                SQL.makeCondition(SQL.makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.WAREHOUSE_ID), tbWarehouseManager.getID(warehouse))

        ));
    }

    XCursor getInventorStock(String name, String warehouse) {
        if (name.equals(warehouse) && name.equals("")) return null;
        if (name.equals("")) {
            return tbProductInStockManager.searchData(SQL.makeAnd(
                    SQL.makeCondition(SQL.makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.WAREHOUSE_ID), tbWarehouseManager.getID(warehouse))
            ));
        } else if (warehouse.equals("")) {
            String pid = tbProductManager.getID(name);
            return tbProductInStockManager.searchData(SQL.makeAnd(
                    SQL.makeCondition(SQL.makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.PID), pid)
            ));
        } else {
            return getUnitInStock(name, warehouse);
        }
    }

    XCursor getWarehouseData() {
        return tbWarehouseManager.selectTable();
    }


    XCursor searchConfirmData(String value) {
        try {
            XCursor cursor;
            if (value.equals("") || value.equals("*")) {
                cursor = tbChecklistManager.getData();
            } else {
                cursor = tbChecklistManager.getData("(" +
                        SQL.makeCondition(SQL.makePointTo(TB_ProductInStockManager.TABLE_NAME, TableColumn.ProductInStockTable.PID), value).concat(" OR ") +
                        SQL.makeLike(SQL.makePointTo(TB_ProductManager.TABLE_NAME, TableColumn.ProductTable.NAME), value) +
                        ")");
            }
            return cursor;
        } catch (SQLiteException sql_ex) {
            Log.e("SQL", sql_ex.toString());
        } catch (CursorIndexOutOfBoundsException cur_ex) {
            Log.e("SQL", cur_ex.toString());
        }
        return null;
    }

    boolean updateColumn(String cid, String value) {
        if (value.equals("")) value = "0";
        try {
            tbChecklistManager.updateColumn(cid, TableColumn.ChecklistTable.CONFIRM, value);
        } catch (SQLiteException ex) {
            return false;
        }
        return true;
    }
}