package com.protienperdollar.redone.trie;

import com.protienperdollar.redone.BuildConfig;

import java.util.ArrayList;
import java.util.List;

class SearchResult<T> {
    final T obj;
    private final int nOfObjectKeywords;
    private int nOfMatches = 0;
    private boolean[] matchTable;
    private float matchProportion = 0.0f;

    SearchResult(T obj, int searchIndex, int storedIndex, int nOfSearchKeywords, int nOfObjKeywords) {
        this.obj = obj;
        this.nOfObjectKeywords = nOfObjKeywords;
        matchTable = new boolean[nOfSearchKeywords];
        if (searchIndex == storedIndex) {
            matchTable[searchIndex] = true;
        }
        newMatch();
    }

    SearchResult<T> intersect(SearchResult<T> other) {
        if (BuildConfig.DEBUG && !obj.equals(other.obj)) throw new RuntimeException("cannot intersect with a result with different obj association");
        newMatch();
        for (int i = 0; i < matchTable.length; i++) {
            if (other.matchAt(i)) {
                matchTable[i] = true;
            }
        }
        return this;
    }

    boolean matchAt(int index) {
        return matchTable[index];
    }

    float matchProportion() {
        return matchProportion;
    }

    T getObj() {
        return obj;
    }

    public boolean equals(Object other) {
        if (other.getClass().toString().equals(getClass().toString())) {
            SearchResult<T> otherSR = (SearchResult<T>) other;
            return otherSR.obj.equals(obj);
        }
        return false;
    }

    public int hashCode() {
        return obj.hashCode();
    }

    private void newMatch() {
        nOfMatches++;
        matchProportion = ((float)nOfMatches) / nOfObjectKeywords;
    }
}
