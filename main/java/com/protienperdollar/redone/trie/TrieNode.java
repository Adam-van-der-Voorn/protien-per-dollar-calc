package com.protienperdollar.redone.trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TrieNode
 * Recursive data structure that stores stops in a format that allows easy searching of them.
 * */

class TrieNode<T> extends AbstractTrie<T>{
    private final Character character;
    private Map<T, ObjectAssocation<T>> associatedObjects = new HashMap<>();
    private TrieNode<T> parent;

    TrieNode(Character character, TrieNode<T> parent) {
        this.character = character;
        this.parent = parent;
    }

    Character getChar() {
        return character;
    }

    void addAssociation(ObjectAssocation<T> association) {
        associatedObjects.put(association.obj, association);
    }
    void removeAssociation(T obj) {
        associatedObjects.remove(obj);
    }

    /**
     * Recursive function
     * */
    Set<ObjectAssocation<T>> getChildAssociations(Set<ObjectAssocation<T>> childAssociations) {
        if (!associatedObjects.isEmpty()) {
            childAssociations.addAll(associatedObjects.values());
        }
        for (TrieNode<T> child : childCharacters.values()) {
            child.getChildAssociations(childAssociations);
        }
        return childAssociations;
    }

    Collection<ObjectAssocation<T>> getAssociatedObjects() {
        return Collections.unmodifiableCollection(associatedObjects.values());
    }

    void pass(String word, TrieNode.ObjectAssocation<T> associated, int index) {
        Character nextChar = word.charAt(index);
        TrieNode<T> nextNode;

        // if the next character is not a child of this node
        if (!childCharacters.containsKey(nextChar)) {
            nextNode = new TrieNode<>(nextChar, this);
            childCharacters.put(nextChar, nextNode);
        }
        else {
            nextNode = childCharacters.get(nextChar);
        }
        assert nextNode != null;
        // if the next character is the final character
        if (index == word.length()-1) {
            nextNode.addAssociation(associated);
        }
        else {
            nextNode.pass(word, associated, index+1);
        }
    }

    Set<T> collect() {
        Set<T> collection = new HashSet<>();
        for (ObjectAssocation<T> objectAssociation : associatedObjects.values()) {
            collection.add(objectAssociation.obj);
        }
        for (TrieNode<T> child : childCharacters.values()) {
            collection.addAll(child.collect());
        }
        return collection;
    }

    TrieNode<T> getParent() {
        return parent;
    }

    /**
     * @return a string representing this node and all its children
     */
    public String toString() {
        String result;
        if (childCharacters.isEmpty()) {
            result = character.toString();
        } else if (childCharacters.size() == 1) {
            TrieNode<T> onlyChild = childCharacters.values().iterator().next();
            result = character.toString() + onlyChild.toString();
        } else {
            StringBuilder str = new StringBuilder();
            str.append(character);
            str.append("{");
            // sort children
            List<TrieNode<T>> children = new ArrayList<>(childCharacters.values());
            children.sort((a, b) -> a.getChar() - b.getChar());
            for (int i = 0; i < children.size(); i++) {
                str.append(children.get(i).toString());
                if (i < children.size() - 1) {
                    str.append(",");
                }
            }
            str.append("}");
            result = str.toString();
        }

        return result;
    }

    static class ObjectAssocation<T> {
        T obj;
        int keywordIndex;
        int nOfKeywords;
        ObjectAssocation(T obj, int keywordIndex, int nOfKeywords) {
            this.obj = obj;
            this.keywordIndex = keywordIndex;
            this.nOfKeywords = nOfKeywords;
        }
    }
}

