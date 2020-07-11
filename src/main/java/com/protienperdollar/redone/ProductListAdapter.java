package com.protienperdollar.redone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.protienperdollar.redone.R;

import java.util.Iterator;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    String[] dataSet;
    ProductClickListener productClickListener;

    public ProductListAdapter(ProductClickListener productClickListener) {
        this.productClickListener = productClickListener;
        dataSet = new String[GlobalData.savedProducts.size()];
        Iterator<String> iter = GlobalData.savedProducts.keySet().iterator();
        for (int i = 0; i < dataSet.length; i++) {
            dataSet[i] = iter.next();
        }
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView productName, productPPD;
        public ProductClickListener productClickListener;
        private String[] dataSet;

        public ViewHolder(View v, String[] dataSet, ProductClickListener productClickListener) {
            super(v);
            productName = v.findViewById(R.id.productName);
            productPPD = v.findViewById(R.id.productPPD);
            this.dataSet = dataSet;
            this.productClickListener = productClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            productClickListener.onProductClick(dataSet[getAdapterPosition()]);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_list, parent, false);

        ViewHolder vh = new ViewHolder(view, dataSet, productClickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(dataSet[position]);
        float productPPD = GlobalData.savedProducts.get(dataSet[position]).getProteinPerDollar();
        holder.productPPD.setText(String.format("%.1fg", productPPD));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    public interface ProductClickListener {
        void onProductClick(String productName);
    }
}