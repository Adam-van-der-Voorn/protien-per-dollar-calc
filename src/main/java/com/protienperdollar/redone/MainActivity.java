package com.protienperdollar.redone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.protienperdollar.redone.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/* TODO:
* > Trie crashes sif searching for only an emoji
* >
* */
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.protienperdollar.";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbarMain));

        loadProductsFromFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewProductList:
                viewSavedProducts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calculate(View view) {
        EditText priceInput = findViewById(R.id.priceInput);
        EditText weightInput = findViewById(R.id.weightInput);
        EditText protienInput = findViewById(R.id.protienInput);
        try {
            float price = Float.parseFloat(priceInput.getText().toString());
            float weight = Float.parseFloat(weightInput.getText().toString());
            float protienPer100g = Float.parseFloat(protienInput.getText().toString());
            Intent intent = new Intent(this, DisplayResultsActivity.class);
            intent.putExtra(EXTRA_MESSAGE + "PRICE", price);
            intent.putExtra(EXTRA_MESSAGE + "WEIGHT", weight);
            intent.putExtra(EXTRA_MESSAGE + "PROTEIN", protienPer100g);
            startActivity(intent);
        }
        catch (NumberFormatException e){
            Snackbar.make(view, "fill out the whole form bro", Snackbar.LENGTH_LONG).show();
        }
    }

    public void viewSavedProducts() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    public void loadProductsFromFile() {
        File savedProducts = new File(getFilesDir(), GlobalData.savedProductsFN);
        if (savedProducts.length() != 0) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(savedProducts));
                int productsAmount = Integer.parseInt(in.readLine());
                for (int i = 0; i < productsAmount; i++) {
                    Product tmp = new Product(in);
                    GlobalData.savedProducts.put(tmp.getName(), tmp);
                    GlobalData.savedProductTrie.put(tmp.getName(), tmp);
                }
                in.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }


    public void clearSavedProducts(View view) {
        File savedProducts = new File(getFilesDir(), GlobalData.savedProductsFN);
        try {
            FileWriter out = new FileWriter(savedProducts);
            out.write("");
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
