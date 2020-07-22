package com.protienperdollar.redone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.ProductClickListener {
    static final String EXTRA_MESSAGE = "com.example.protienperdollar.";
    static final String TAG = "ProductListActivity";
    ProductListAdapter adapter;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setSupportActionBar(findViewById(R.id.toolbarProductList));

        RecyclerView recyclerView = findViewById(R.id.productList);
        adapter = new ProductListAdapter();
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        searchBar = findViewById(R.id.productSearchInput);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayProducts(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "search bar contents (str): [" + searchBar.getText().toString() + "]");
        displayProducts(searchBar.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product_list_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortBy:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE + "NAME", product.getName());
        startActivity(intent);
    }

    public void displayProducts(String search) {
        if (search.equals("")) {
            adapter.submitList(new ArrayList<>(GlobalData.savedProducts.values()));
        }
        else adapter.submitList(GlobalData.savedProductTrie.search(search));
    }
}
