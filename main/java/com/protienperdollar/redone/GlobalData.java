package com.protienperdollar.redone;

import android.app.Application;

import com.protienperdollar.redone.trie.Trie;

import java.util.HashMap;
import java.util.Map;

class GlobalData extends Application {
    static String savedProductsFN = "saved_products.txt";
    static Map<String, Product> savedProducts = new HashMap<>();
    static Trie savedProductTrie = new Trie();


}
