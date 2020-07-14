package com.protienperdollar.redone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.ProductViewHolder> {
    private ProductClickListener productClickListener;

    public ProductListAdapter() {
        super(new DiffUtil.ItemCallback<Product>() {
            @Override
            public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of products to represent an item.
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View listElementView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_list, parent, false);
        return new ProductViewHolder(listElementView, getCurrentList(), productClickListener);
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = getItem(position);
        holder.bindTo(product);
    }

    // Provide a reference to the views for each data item
    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView productName, productPPD;
        public ProductClickListener productClickListener;
        private List<Product> dataSet;

        public ProductViewHolder(View v, List<Product> dataSet, ProductClickListener productClickListener) {
            super(v);
            productName = v.findViewById(R.id.productName);
            productPPD = v.findViewById(R.id.productPPD);
            this.dataSet = dataSet;
            this.productClickListener = productClickListener;
            itemView.setOnClickListener(this);
        }

        public void bindTo(Product product) {
            productName.setText(product.getName());
            productPPD.setText(String.format(Locale.ENGLISH,"%.1f", product.getProteinPerDollar()));
        }

        @Override
        public void onClick(View view) {
            productClickListener.onProductClick(dataSet.get(getAdapterPosition()));
        }
    }

    public void setClickListener(ProductClickListener p) {
        productClickListener = p;
    }

    public interface ProductClickListener {
        void onProductClick(Product product);
    }
}