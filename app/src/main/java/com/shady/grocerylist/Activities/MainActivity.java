package com.shady.grocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.shady.grocerylist.Data.DatabaseHandler;
import com.shady.grocerylist.Model.Grocery;
import com.shady.grocerylist.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dailogBuilder;
    private AlertDialog dailog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopUpDailog();
            }
        });

        byPassActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopUpDailog(){
        dailogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = view.findViewById(R.id.groceryItem);
        quantity = view.findViewById(R.id.groceryQty);
        saveButton = view.findViewById(R.id.saveButton);

        dailogBuilder.setView(view);
        dailog = dailogBuilder.create();
        dailog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:  save to db
                if(!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty())
                    saveGroceryToDB(v);
                //todo: go to next screen
                

            }
        });
    }

    private void saveGroceryToDB(View view) {

        Grocery grocery = new Grocery();
        String newGrocery = groceryItem.getText().toString();
        String newQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newQuantity);

        //save to db
        Log.d("before", "saveGroceryToDB: ");
        db.addGrocery(grocery);
        Log.d("after", "saveGroceryToDB: ");
        Snackbar.make(view, "Data Saved! ", Snackbar.LENGTH_SHORT).show();

        //check if data added
//        Log.d("DBcount", "saveGroceryToDB: "+String.valueOf(db.getGroceriesCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             dailog.dismiss();
             startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        },1200);
    }

    public void byPassActivity(){
        if (db.getGroceriesCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }
}