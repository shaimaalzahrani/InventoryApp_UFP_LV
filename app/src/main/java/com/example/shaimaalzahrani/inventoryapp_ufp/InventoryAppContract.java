package com.example.shaimaalzahrani.inventoryapp_ufp;

import android.provider.BaseColumns;

public class InventoryAppContract {

    public InventoryAppContract() {
    }

    public static final class StockEntry implements BaseColumns {

        public static final String TABLE_NAME = "INV";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";
        public static final String COLUMN_SUPPLIER_PHONE = "supplier_phone";
        public static final String COLUMN_IMAGE = "image";

        public static final String CREATE_TABLE_INV = "CREATE TABLE " +
                InventoryAppContract.StockEntry.TABLE_NAME + "(" +
                InventoryAppContract.StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                InventoryAppContract.StockEntry.COLUMN_NAME + " TEXT NOT NULL," +
                InventoryAppContract.StockEntry.COLUMN_PRICE + " TEXT NOT NULL," +
                InventoryAppContract.StockEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL," +
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL," +
                StockEntry.COLUMN_IMAGE + " TEXT NOT NULL" + ");";
    }
}