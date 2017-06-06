package com.example.shaimaalzahrani.inventoryapp_ufp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ItemDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private InventoryAppDbHelper dbHelper;
    EditText ETName;
    EditText ETPrice;
    EditText ETQuantity;
    EditText ETSupplierName;
    EditText ETSupplierEmail;
    EditText ETSupplierPhone;
    long currentId;
    ImageButton incQuantity;
    ImageButton decQuantity;
    Button imgBtn;
    ImageView imgViw;
    Uri TheUri;
    private static final int SELECT_IMAGE = 0;
    Boolean infoItemChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUp();

        if (currentId == 0) {
            setTitle(getString(R.string.activity_title_new_item));
        } else {
            setTitle(getString(R.string.activity_title_edit_item));
            addValuesToInterface(currentId);
        }

        decQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subOneFromQuantity();
                infoItemChanged = true;
            }
        });

        incQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOneToQuantity();
                infoItemChanged = true;
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageSelector();
                infoItemChanged = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!infoItemChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
        // this will till the user that you clicked back without saving
    }

    // the dialog of back without saving
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*
    * those below two methods are for incrementing an decrementing the Quantity
    * */

    private void subOneFromQuantity() {
        String previousValS = ETQuantity.getText().toString();
        int previousVal;
        if (previousValS.isEmpty() || previousValS.equals("0")) {
            return;
        } else {
            previousVal = Integer.parseInt(previousValS);
            ETQuantity.setText(String.valueOf(previousVal - 1));
        }
    }

    private void addOneToQuantity() {
        String previousValS = ETQuantity.getText().toString();
        int previousVal;
        if (previousValS.isEmpty()) {
            previousVal = 0; // initial value for Quantity
        } else {
            previousVal = Integer.parseInt(previousValS);
        }
        ETQuantity.setText(String.valueOf(previousVal + 1));
    }

    // adding the menu to the view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    // setting the options of the menu to hidden when there is no items
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem deleteOneItemMenuItem = menu.findItem(R.id.action_delete_item);
        MenuItem deleteAllMenuItem = menu.findItem(R.id.action_delete_all_data);
        MenuItem orderMenuItem = menu.findItem(R.id.action_order);
        if (currentId == 0) {
            deleteOneItemMenuItem.setVisible(false);
            deleteAllMenuItem.setVisible(false);
            orderMenuItem.setVisible(false);
        }
        else{
            deleteOneItemMenuItem.setVisible(true);
            deleteAllMenuItem.setVisible(true);
            orderMenuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (!ItemAddedToDb()) {
                    return true;
                }
                finish();
                return true;
            case android.R.id.home:
                if (!infoItemChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(ItemDetailsActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            case R.id.action_order:
                showOrderConfirmDialog();
                return true;
            case R.id.action_delete_item:
                showDeleteConfirmDialog(currentId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // deleting the selected item from DB
    private int deleteSelectedItem(long itemId) {
        SQLiteDatabase DB = dbHelper.getWritableDatabase();
        String selection = InventoryAppContract.StockEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(itemId) };
        int DeletedRows = DB.delete(
                InventoryAppContract.StockEntry.TABLE_NAME, selection, selectionArgs);
        return DeletedRows;
    }

    // adding item
    private boolean ItemAddedToDb() {
        boolean flag = true;
        // checking all field are not empty
        if (!checkValEx(ETName, "name")) {
            flag = false;
        }
        if (!checkValEx(ETPrice, "price")) {
            flag = false;
        }
        if (!checkValEx(ETQuantity, "quantity")) {
            flag = false;
        }
        if (!checkValEx(ETSupplierName, "supplier name")) {
            flag = false;
        }
        if (!checkValEx(ETSupplierEmail, "supplier email")) {
            flag = false;
        }
        if (!checkValEx(ETSupplierPhone, "supplier phone")) {
            flag = false;
        }
        if (TheUri == null && currentId == 0) {
            flag = false;
            imgBtn.setError("Missing image");
        }
        if (!flag) {
            return false;
        }

        if (currentId == 0) { // if new item to be added
            Item item = new Item(
                    ETName.getText().toString().trim(),
                    ETPrice.getText().toString().trim(),
                    Integer.parseInt(ETQuantity.getText().toString().trim()),
                    ETSupplierName.getText().toString().trim(),
                    ETSupplierEmail.getText().toString().trim(),
                    ETSupplierPhone.getText().toString().trim(),
                    TheUri.toString());
            dbHelper.insertItem(item);
        } else { // if item exist and need to be updated
            int quantity = Integer.parseInt(ETQuantity.getText().toString().trim());
            dbHelper.updateItemQ(currentId, quantity);
        }
        return true;
    }

    // checking text editor is not empty
    private boolean checkValEx(EditText text, String description) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing product " + description);
            return false;
        } else {
            text.setError(null);
            return true;
        }
    }

    // adding the newly added item to the list in screen (reading)
    private void addValuesToInterface(long itemId) {
        Cursor cursor = dbHelper.readItem(itemId);
        cursor.moveToFirst();
        ETName.setText(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_NAME)));
        ETPrice.setText(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_PRICE)));
        ETQuantity.setText(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_QUANTITY)));
        ETSupplierName.setText(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_SUPPLIER_NAME)));
        ETSupplierEmail.setText(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_SUPPLIER_EMAIL)));
        ETSupplierPhone.setText(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_SUPPLIER_PHONE)));
        imgViw.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(InventoryAppContract.StockEntry.COLUMN_IMAGE))));
        disableALL();
    }

    // opening the image selector dialog
    public void OpenImageSelector() {
        // first checking the permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        // then opening the intent
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*"); // to bring all images
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    // dialog for confirming the order
    private void showOrderConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // by email ( opening mail to intent and putting the To as the supplier email
        builder.setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(android.content.Intent.ACTION_SENDTO); // open send to intent
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + ETSupplierEmail.getText().toString().trim()));
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "New order");
                String bodyMessage = "Please send us more form your" +
                        ETName.getText().toString().trim() +
                        "!!!";
                intent.putExtra(android.content.Intent.EXTRA_TEXT, bodyMessage);
                startActivity(intent);
            }
        });
        // by phone ( opening phone call intent and putting the number of the supplier)
        builder.setMessage(R.string.order_msg);
        builder.setPositiveButton(R.string.phone, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DIAL); // open dial intent
                intent.setData(Uri.parse("tel:" + ETSupplierPhone.getText().toString().trim()));
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // when deleting show deletion confirmation before!
    private void showDeleteConfirmDialog(final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteSelectedItem(itemId); // the id coming from before intent
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent;
                    if (Build.VERSION.SDK_INT < 19) {
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                    } else {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                    }
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                }
            }
        }
    }

    // saving returned image from select intent and showing it
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                TheUri = resultData.getData();
                imgViw.setImageURI(TheUri);
                imgViw.invalidate();
            }
        }
    }

    // disabling all except Quantity
    public void disableALL(){
        ETName.setEnabled(false);
        ETPrice.setEnabled(false);
        ETSupplierName.setEnabled(false);
        ETSupplierEmail.setEnabled(false);
        ETSupplierPhone.setEnabled(false);
        imgBtn.setEnabled(false);
    }

    // initializing the variables and views
    public void setUp(){
        ETName = (EditText) findViewById(R.id.product_name_field);
        ETPrice = (EditText) findViewById(R.id.price_field);
        ETQuantity = (EditText) findViewById(R.id.quantity_field);
        ETSupplierName = (EditText) findViewById(R.id.supplier_name_field);
        ETSupplierEmail = (EditText) findViewById(R.id.supplier_email_field);
        ETSupplierPhone = (EditText) findViewById(R.id.supplier_phone_field);
        decQuantity = (ImageButton) findViewById(R.id.decrease);
        incQuantity = (ImageButton) findViewById(R.id.increase);
        imgBtn = (Button) findViewById(R.id.select_image_B);
        imgViw = (ImageView) findViewById(R.id.image_view);

        dbHelper = new InventoryAppDbHelper(this);
        currentId = getIntent().getLongExtra("itemId", 0);
    }
}