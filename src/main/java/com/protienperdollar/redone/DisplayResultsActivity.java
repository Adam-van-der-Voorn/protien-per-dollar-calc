package com.protienperdollar.redone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.protienperdollar.redone.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;


public class DisplayResultsActivity extends AppCompatActivity {
    private final String TAG = "DisplayResultsActivity";
    private float price, weight, proteinPer100g, protienPerDollar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        // get the intent that started this activity and extract the string
        Intent intent = getIntent();
        price = intent.getFloatExtra(MainActivity.EXTRA_MESSAGE + "PRICE", 1.0f);
        weight = intent.getFloatExtra(MainActivity.EXTRA_MESSAGE + "WEIGHT", 100.0f);
        proteinPer100g = intent.getFloatExtra(MainActivity.EXTRA_MESSAGE + "PROTEIN", 100.0f);

        protienPerDollar = Product.calculatePPD(price, weight, proteinPer100g);

        TextView textView = findViewById(R.id.resultsView);
        textView.setText(String.format(Locale.ENGLISH, "%.1fg", protienPerDollar));
    }

    public void saveProduct(View view) {
        EditText productInput = findViewById(R.id.productInput);
        EditText productNotesInput = findViewById(R.id.productNotesInput);
        String productName = productInput.getText().toString();
        String productNotes = productNotesInput.getText().toString();
        if (!productName.equals("")) {
            Product product = new Product(productName, productNotes, price, weight, proteinPer100g);
            GlobalData.savedProducts.put(productName, product);
            GlobalData.savedProductTrie.put(productName, product);
            productInput.setEnabled(false);
            productNotesInput.setEnabled(false);
            try {
                saveProductsToFile(GlobalData.savedProducts.values());
                Snackbar.make(view, "Product saved!", Snackbar.LENGTH_SHORT);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveProductsToFile(Collection<Product> products) throws IOException {
        File productsOnDisk = new File(getFilesDir(), GlobalData.savedProductsFN);
        FileWriter out = new FileWriter(productsOnDisk);
        out.write(products.size() + "\n");
        Log.v(TAG, String.valueOf(products.size()));
        for (Product product : products) {
            String productData = product.getData();
            Log.v(TAG, productData);
            out.write(productData);
        }
        out.close();
    }
}
