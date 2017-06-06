package com.example.shaimaalzahrani.inventoryapp_ufp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryAppDbHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "inv_.db";
    public final static int DB_VERSION = 1;

    public InventoryAppDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InventoryAppContract.StockEntry.CREATE_TABLE_INV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryAppContract.StockEntry.COLUMN_NAME, item.getProductName());
        values.put(InventoryAppContract.StockEntry.COLUMN_PRICE, item.getPrice());
        values.put(InventoryAppContract.StockEntry.COLUMN_QUANTITY, item.getQuantity());
        values.put(InventoryAppContract.StockEntry.COLUMN_SUPPLIER_NAME, item.getSupplierName());
        values.put(InventoryAppContract.StockEntry.COLUMN_SUPPLIER_EMAIL, item.getSupplierEmail());
        values.put(InventoryAppContract.StockEntry.COLUMN_SUPPLIER_PHONE, item.getSupplierPhone());
        values.put(InventoryAppContract.StockEntry.COLUMN_IMAGE, item.getImage());
        long id = db.insert(InventoryAppContract.StockEntry.TABLE_NAME, null, values);
    }

    public Cursor readAll() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                InventoryAppContract.StockEntry._ID,
                InventoryAppContract.StockEntry.COLUMN_NAME,
                InventoryAppContract.StockEntry.COLUMN_PRICE,
                InventoryAppContract.StockEntry.COLUMN_QUANTITY,
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_NAME,
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_EMAIL,
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_PHONE,
                InventoryAppContract.StockEntry.COLUMN_IMAGE
        };
        Cursor cursor = db.query(
                InventoryAppContract.StockEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor readItem(long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                InventoryAppContract.StockEntry._ID,
                InventoryAppContract.StockEntry.COLUMN_NAME,
                InventoryAppContract.StockEntry.COLUMN_PRICE,
                InventoryAppContract.StockEntry.COLUMN_QUANTITY,
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_NAME,
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_EMAIL,
                InventoryAppContract.StockEntry.COLUMN_SUPPLIER_PHONE,
                InventoryAppContract.StockEntry.COLUMN_IMAGE
        };
        String selection = InventoryAppContract.StockEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(itemId) };

        Cursor cursor = db.query(
                InventoryAppContract.StockEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    public void updateItemQ(long currentItemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryAppContract.StockEntry.COLUMN_QUANTITY, quantity);
        String selection = InventoryAppContract.StockEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(currentItemId) };
        db.update(InventoryAppContract.StockEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

    public void sellOneItem(long itemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity -1;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryAppContract.StockEntry.COLUMN_QUANTITY, newQuantity);
        String selection = InventoryAppContract.StockEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(itemId) };
        db.update(InventoryAppContract.StockEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }
}
