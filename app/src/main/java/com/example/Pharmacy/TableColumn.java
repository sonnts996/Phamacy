package com.example.Pharmacy;

@SuppressWarnings("ALL")
public class TableColumn {
    public class ID {
        static final String STOCK_ID = "Stock_ID";
        static final String PID = "PID";
        static final String CID = "CID";
        static final String BARCODE_ID = "Barcode_ID";
        static final String WAREHOUSE_ID = "Warehouse_ID";
        static final String CATEGORIES_ID = "Categories_ID";
        static final String QUEUE_ID = "Queue_ID";
        static final String UNIT_ID = "Unit_ID";
        static final String ACCOUNT_ID = "Account_ID";
    }

    public class AccountTable {
        public static final String TABLE = "AccountTable";
        public static final String ID = TableColumn.ID.ACCOUNT_ID;
        public static final String USERNAME = "Username";
        public static final String PASSWORD = "Password";
    }

    public class UnitTable {
        public static final String TABLE = "UnitTable";
        public static final String ID = TableColumn.ID.UNIT_ID;
        public static final String UNIT = "Unit";
        public static final String UNIT2 = "Unit2";
        public static final String FACTOR2 = "Factor2";
        public static final String UNIT3 = "Unit3";
        public static final String FACTOR3 = "Factor3";
    }

    public class QueueTable {
        public static final String TABLE = "QueueTable";
        public static final String ID = TableColumn.ID.QUEUE_ID;
        public static final String QUEUE = "Queue";
    }

    public class ProductTable {
        public static final String TABLE = "ProductTable";
        public static final String ID = TableColumn.ID.PID;
        public static final String NAME = "Name";
    }

    public class WarehouseTable {
        public static final String TABLE = "WarehouseTable";
        public static final String ID = TableColumn.ID.WAREHOUSE_ID;
        public static final String WAREHOUSE = "Warehouse";
    }

    public class BarCodeTable {
        public static final String TABLE = "BarcodeTable";
        public static final String ID = TableColumn.ID.BARCODE_ID;
        public static final String BARCODE = "Barcode";
        public static final String PID = TableColumn.ID.PID;
    }

    public class CategoriesTable {
        public static final String TABLE = "CategoriesTable";
        public static final String ID = TableColumn.ID.CATEGORIES_ID;
        public static final String CATEGORIES = "Categories";
    }

    public class ProductInStockTable {
        public static final String TABLE = "ProductInStockTable";
        public static final String STOCK_ID = TableColumn.ID.STOCK_ID;
        public static final String PID = TableColumn.ID.PID;
        public static final String TOTAL = "Total"; //must be factor1
        public static final String UNIT_ID = TableColumn.ID.UNIT_ID;
        public static final String WAREHOUSE_ID = TableColumn.ID.WAREHOUSE_ID;
        public static final String QUEUE_ID = TableColumn.ID.QUEUE_ID;
        public static final String CATEGORIES_ID = TableColumn.ID.CATEGORIES_ID;
        public static final String DATE = "Date";
        public static final String PRICE = "Price";
    }

    public class HistoriesChecklistTable {
        public static final String TABLE = "HistoriesChecklistTable";
        public static final String ID = "Histories_ID";
        public static final String CID = TableColumn.ID.CID;
        public static final String PID = TableColumn.ID.PID;
        public static final String REAL = "Real";
        public static final String UNIT_ID = TableColumn.ID.UNIT_ID;
        public static final String WAREHOUSE_ID = TableColumn.ID.WAREHOUSE_ID;
        public static final String QUEUE_ID = TableColumn.ID.QUEUE_ID;
        public static final String CATEGORIES_ID = TableColumn.ID.CATEGORIES_ID;
        public static final String DATE = "Date";
        public static final String SYNCCOUNT = "SyncCount";
        public static final String SYNCTIME = "SyncTime";
        public static final String SYNCACCOUNT = "SyncAccount";
        public static final String BARCODE = "BarCode";
    }

    public class ChecklistTable {
        public static final String TABLE = "ChecklistTable";
        public static final String CID = TableColumn.ID.CID;
        public static final String PID = TableColumn.ID.PID;
        public static final String REAL = "Real";
        public static final String UNIT_ID = TableColumn.ID.UNIT_ID;
        public static final String WAREHOUSE_ID = TableColumn.ID.WAREHOUSE_ID;
        public static final String QUEUE_ID = TableColumn.ID.QUEUE_ID;
        public static final String CATEGORIES_ID = TableColumn.ID.CATEGORIES_ID;
        public static final String DATE = "Date";
        public static final String RECORDTIME = "RecordTime";
        public static final String CONFIRM = "Confirm";
    }
}
