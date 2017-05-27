package com.example.shaimaalzahrani.inventoryapp_ufp;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.shaimaalzahrani.inventoryapp_ufp.data.InventoryDbHelper;
import com.example.shaimaalzahrani.inventoryapp_ufp.data.StockItem;


public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getCanonicalName();
    InventoryDbHelper dbHelper;
    StockCursorAdapter adapter;
    int lastVisibleItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addDummyData();
        dbHelper = new InventoryDbHelper(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    /**
     * Add data for demo purposes
     */
    private void addDummyData() {
        StockItem gummibears = new StockItem(
                "Gummibears",
                "10 SAR",
                45,
                "Shaima Alzahrani",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/gummybear.png");
        dbHelper.insertItem(gummibears);

        StockItem cola = new StockItem(
                "Cola",
                "6 SAR",
                44,
                "Shaima Alzahrani",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/cola");
        dbHelper.insertItem(cola);

        StockItem fruitSalad = new StockItem(
                "Fruit salad",
                "20 SAR",
                34,
                "Shaima Alzahrani",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/fruit_salad");
        dbHelper.insertItem(fruitSalad);

        StockItem lolipopStrawberry = new StockItem(
                "Lolipop strawberry",
                "12 SAR",
                62,
                "Shaima Alzahrani",
                "android.resource://com.example.shaimaalzahrani.inventoryapp_ufp/drawable/lolipop");
        dbHelper.insertItem(lolipopStrawberry);

    }

    public void OpenD(View view) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        startActivity(intent);
    }
}
