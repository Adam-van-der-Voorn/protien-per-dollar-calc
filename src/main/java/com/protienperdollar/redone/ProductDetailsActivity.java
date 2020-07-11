package com.protienperdollar.redone;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.protienperdollar.redone.R;

public class ProductDetailsActivity extends AppCompatActivity {
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarProductDetails));


        String productName = getIntent().getStringExtra(ProductListActivity.EXTRA_MESSAGE + "NAME");
        product = GlobalData.savedProducts.get(productName);

        setDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product_details_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.productEdit:
                return true;
            case R.id.productDelete:
                GlobalData.savedProducts.remove(product.getName());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDetails() {
        getSupportActionBar().setTitle(product.getName());
        TextView ppdLabel = findViewById(R.id.ppdLabel);
        TextView pp100gLabel = findViewById(R.id.pp100gLabel);
        TextView ppkLabel = findViewById(R.id.ppkLabel);
        ppdLabel.setText(String.format("%.1f", product.getProteinPerDollar()) + "g");
        pp100gLabel.setText(String.format("%.1f", product.getProteinPer100g()) + "g");
        ppkLabel.setText("$" + String.format("%.2f", product.getPricePerKilo()));
    }
}
