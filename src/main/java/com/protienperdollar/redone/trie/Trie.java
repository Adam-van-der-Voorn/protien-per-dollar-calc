package com.protienperdollar.redone.trie;

import com.protienperdollar.redone.trie.exceptions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * SPECIFICATIONS:
 *
 * any string can be placed in the trie, with an object associated with it.
 * when searching for a string, the search string is split by whitespace into keywords.
 * if a string that was passed into the trie contains a keyword, the object associated with that string becomes a candidate.
 * the candidates are returned as a List.
 * aside from a candidates actual properties, a candidates position in the list can be dictated by the following:
 * > proportion of keywords are found
 * > the amount of keywords found
 * > correct order of keywords
 * a comparator can be given to define the exact ordering of the elemements.
 * */

public class Trie<T> {
    private Comparator<SearchResult<T>> comparator;
    private Pattern toConcat;
    private Pattern toSkip;
    private TrieNode<T> rootNode = new TrieNode<>(null, null);

    public Trie() {
        toConcat = Pattern.compile("'");
        toSkip = Pattern.compile("[^a-zA-Z0-9&]+");
        comparator = (a, b) -> 0;
    }

    public Trie(Comparator<SearchResult<T>> comparator) {
        this.comparator = comparator;
        this.toConcat = Pattern.compile("'");
        this.toSkip = Pattern.compile("[^a-zA-Z0-9&]+");
    }

    public Trie(Pattern toConcat, Pattern toSkip) {
        this.comparator = (a, b) -> 0;
        this.toConcat = toConcat;
        this.toSkip = toSkip;
    }

    public Trie(Comparator<SearchResult<T>> comparator, Pattern toConcat, Pattern toSkip) {
        this.comparator = comparator;
        this.toConcat = toConcat;
        this.toSkip = toSkip;
    }

    /**
     * Searches the trie for any objects that are associated with the given name.
     *
     * @param name the name of the object to search for.
     * @return a list of all the objects associated with the input name, ordered by the comparator in this trie object.
     * an empty search input returns an empty list.
     * */
    public List<T> search(String name) {
        if (name.equals("")) {
            return new ArrayList<>();
        }
        List<String> keywords = processName(name);
        try {
            Map<T, SearchResult<T>> resultsA = searchForKeyword(keywords,0);
            for (int i = 1; i < keywords.size(); i++) {
                Map<T, SearchResult<T>> resultsB = searchForKeyword(keywords, i);
                resultsA = resultsA.entrySet().stream()
                        .filter((entry) -> resultsB.containsKey(entry.getKey()))
                        .map((entry) -> entry.getValue().intersect(resultsB.get(entry.getKey())))
                        .collect(Collectors.toMap(SearchResult::getObj, Function.identity()));
            }
            List<SearchResult<T>> results = new ArrayList<>(resultsA.values());
            Collections.sort(results, comparator.reversed());
            return results.stream()
                    .map((e) -> e.obj)
                    .collect(Collectors.toList());
        } catch (NoAssociatedObjectsException e) {
            return new ArrayList<>();
        }
    }

    /**
     * puts an object in the trie.
     * @param name the name of the object. The name is used as a search keyword. //TODO make more professional
     * @param associated the object that is associated with the given name.
     * */
    public void put(String name, T associated) {
        List<String> keywords = processName(name);
        for (int i = 0; i < keywords.size(); i++) {
            rootNode.pass(keywords.get(i), new TrieNode.ObjectAssocation<>(associated, i, keywords.size()), 0);
        }
    }

    /**
     * removes an object from the trie.
     * */
    public void remove(String name, T associated) throws NoAssociatedObjectsException {
        List<String> keywords = processName(name);
        for (String keyword : keywords) {
            removeKeyword(associated, keyword);
        }
    }

    public Set<T> getAll() {
        return rootNode.collect();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("root{");

        // sort children for predictability
        List<TrieNode<T>> children = new ArrayList<>(rootNode.getChildren());
        children.sort((a, b) -> a.getChar() - b.getChar());

        for (int i = 0; i < children.size(); i++) {
            str.append(children.get(i).toString());
            if (i < children.size()-1) {
                str.append(",");
            }
        }
        str.append("}");
        return str.toString();
    }

    private List<String> processName(String name) {
        name = toConcat.matcher(name).replaceAll("");
        List<String> keywords = new ArrayList<>();
        Scanner in = new Scanner(name);
        in.useDelimiter(toSkip);
        while (in.hasNext()) {
            keywords.add(in.next().toLowerCase());
        }
        return keywords;
    }

    private Map<T, SearchResult<T>> searchForKeyword(List<String> keywords, int keywordIndex) throws NoAssociatedObjectsException{
        TrieNode<T> topNode = rootNode.getNode(keywords.get(keywordIndex), 0);
        Set<TrieNode.ObjectAssocation<T>> associations = topNode.getChildAssociations(new HashSet<>());
        return associations.stream()
                .map((e) -> new SearchResult<>(e.obj, keywordIndex, e.keywordIndex, keywords.size(), e.nOfKeywords))
                .collect(Collectors.toMap(SearchResult::getObj, Function.identity(), (existing, replacement) -> existing));
    }

    private void removeKeyword(T associated, String keyword) throws NoAssociatedObjectsException {
        TrieNode<T> node = rootNode.getNode(keyword, 0);

        // case where the keyword to remove is not a leaf node,
        // or where the keyword to remove is a leaf node with multiple associations
        if (!node.getChildren().isEmpty() || node.getAssociatedObjects().size() > 1) {
            node.removeAssociation(associated);
            return;
        }

        TrieNode<T> parent = node.getParent();
        int i = keyword.length()-1;
        while (parent.getChildren().size() == 1 && parent.getAssociatedObjects().size() == 0) {
            node = parent;
            parent = node.getParent();
            i--;
        }
        parent.removeChild(keyword.charAt(i));
    }
}
