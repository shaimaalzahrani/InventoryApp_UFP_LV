package com.example.shaimaalzahrani.inventoryapp_ufp;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    InventoryAppDbHelper dbHelper;
    InventoryAppCursorAdapter adapter;
    int lastItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new InventoryAppDbHelper(this);

        // Adding button
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ADDING);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                startActivity(intent);
            }
        });

        // list view
        final ListView listView = (ListView) findViewById(R.id.list_view);
        View emptyView = findViewById(R.id.emp_view);
        listView.setEmptyView(emptyView);

        // fill the list
        Cursor cursor = dbHelper.readAll();

        adapter = new InventoryAppCursorAdapter(this, cursor);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0) return;
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > lastItem) {
                    fab.show();
                } else if (currentFirstVisibleItem < lastItem) {
                    fab.hide();
                }
                lastItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    // to update if any addition happens
    @Override
    protected void onResume() {
        super.onResume();
        adapter.swapCursor(dbHelper.readAll());
    }

    // open detail activity and send the item id
    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }

    // this will sell one item (decrement the quantity by 1)
    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        adapter.swapCursor(dbHelper.readAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_dummy_data:
                addDummyData();
                adapter.swapCursor(dbHelper.readAll());
        }
        return super.onOptionsItemSelected(item);
    }

    // dummy data
    private void addDummyData() {
        Item thefault = new Item("The Fault In Our Stars", "19 SAR", 50, "Shaima Alzahrani", "+966 00 000 0000",
                "shaima@inv.com", "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/thefault");
        dbHelper.insertItem(thefault);

        Item papertawn = new Item("Paper Towns", "17 SAR", 43, "Shaima Alzahrani", "+966 00 000 0000", "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/papertawn");
        dbHelper.insertItem(papertawn);

        Item harrybook = new Item("Harry Poter", "20 SAR", 32, "Shaima Alzahrani", "+966 00 000 0000", "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/harrypoter");
        dbHelper.insertItem(harrybook);

        Item thift = new Item("The Book Thift", "12 SAR", 22, "Shaima Alzahrani", "+966 00 000 0000", "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/bookthift");
        dbHelper.insertItem(thift);
    }
}