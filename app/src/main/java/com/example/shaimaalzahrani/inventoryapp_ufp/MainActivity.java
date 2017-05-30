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

import com.example.shaimaalzahrani.inventoryapp_ufp.data.InventoryDbHelper;
import com.example.shaimaalzahrani.inventoryapp_ufp.data.StockItem;


public class MainActivity extends AppCompatActivity {

    InventoryDbHelper dbHelper;
    StockCursorAdapter adapter;
    int lastVisibleItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new InventoryDbHelper(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.list_view);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        Cursor cursor = dbHelper.readStock();

        adapter = new StockCursorAdapter(this, cursor);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0) return;
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > lastVisibleItem) {
                    fab.show();
                } else if (currentFirstVisibleItem < lastVisibleItem) {
                    fab.hide();
                }
                lastVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.swapCursor(dbHelper.readStock());
    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }

    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        adapter.swapCursor(dbHelper.readStock());
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
                // add dummy data for testing
                addDummyData();
                adapter.swapCursor(dbHelper.readStock());
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add data for demo purposes
     */
    private void addDummyData() {
        StockItem gummibears = new StockItem(
                "Gummybears",
                "10 SAR",
                66,
                "Shaima Alzahrani",
                "+966 00 000 0000",
                "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/gummybear");
        dbHelper.insertItem(gummibears);

        StockItem cola = new StockItem(
                "Cola",
                "13 SAR",
                78,
                "Shaima Alzahrani",
                "+966 00 000 0000",
                "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/cola");
        dbHelper.insertItem(cola);

        StockItem fruitSalad = new StockItem(
                "Fruit salad",
                "20 SAR",
                32,
                "Shaima Alzahrani",
                "+966 00 000 0000",
                "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/fruit_salad");
        dbHelper.insertItem(fruitSalad);

        StockItem lolipop = new StockItem(
                "Lolipop strawberry",
                "12 SAR",
                22,
                "Shaima Alzahrani",
                "+966 00 000 0000",
                "shaima@inv.com",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/lolipop");
        dbHelper.insertItem(lolipop);

    }
}
