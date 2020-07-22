package com.protienperdollar.redone.util;

import android.util.Log;

import com.protienperdollar.redone.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class IOFunctions {
    private static final String TAG = "IOFunctions";
    public static void saveProductsToFile(Collection<Product> products, File file) throws IOException {
        FileWriter out = new FileWriter(file);
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
