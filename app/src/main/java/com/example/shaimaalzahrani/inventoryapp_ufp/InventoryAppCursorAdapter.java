package com.example.shaimaalzahrani.inventoryapp_ufp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InventoryAppCursorAdapter extends CursorAdapter {


    private final MainActivity activity;

    public InventoryAppCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTV = (TextView) view.findViewById(R.id.product_name);
        TextView quantityTV = (TextView) view.findViewById(R.id.quantity);
        TextView priceTV = (TextView) view.findViewById(R.id.price);
        ImageView sale = (ImageView) view.findViewById(R.id.sale);
        ImageView image = (ImageView) view.findViewById(R.id.image_view);

        String name = cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_QUANTITY));
        String price = cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_PRICE));
        image.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_IMAGE))));

        nameTV.setText(name);
        quantityTV.setText(String.valueOf(quantity));
        priceTV.setText(price);

        final long id = cursor.getLong(cursor.getColumnIndex(InventoryAppContract.StockEntry._ID));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.clickOnViewItem(id);
            }
        });

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.clickOnSale(id, quantity);
            }
        });
    }
}
