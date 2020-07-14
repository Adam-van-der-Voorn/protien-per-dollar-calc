package com.protienperdollar.redone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.ProductClickListener {
    static final String EXTRA_MESSAGE = "com.example.protienperdollar.";
    ProductListAdapter adapter;

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
        adapter.submitList(new ArrayList<>(GlobalData.savedProducts.values()));
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

    //public void onSearch(String search) {
    //    adapter.submitList(GlobalData.savedProductTrie.search(search));
    //}
}
