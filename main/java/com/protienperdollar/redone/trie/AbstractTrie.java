package com.protienperdollar.redone.trie;

import com.protienperdollar.redone.trie.exceptions.NoAssociatedObjectsException;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractTrie<T> {
    protected Map<Character, TrieNode<T>> childCharacters = new HashMap<>();
    /**
     * @param searchString the sequence of child characters to find
     * @param index the index in the search string of the next character to look for
     * @return the node in the branch that corresponds to the final character in the search string.
     * @throws NoAssociatedObjectsException if no sequence of nodes that match the input string can be found.
     * */
    protected TrieNode<T> getNode(String searchString, int index) throws NoAssociatedObjectsException {
        Character workingChar = searchString.charAt(index);
        if (childCharacters.containsKey(workingChar)) {
            if (index == searchString.length()-1) {
                return childCharacters.get(workingChar);
            }
            return childCharacters.get(workingChar).getNode(searchString, index+1);
        }
        else throw new NoAssociatedObjectsException();
    }

    /** Recursive function used to insert a new name into the trie.
     * > if the name is one character long, the node is marked as the end of a word and the function is terminated.
     * > otherwise:
     * > a child node is identified that's character is the same as the first character in the input string.
     * > if this node cannot be found, a new child node is created and given the first character of the input string.
     * > the fist character is omitted from the input string and passed to the child node, where the process starts again.
     * */
    abstract void pass(String word, TrieNode.ObjectAssocation<T> associated, int index);

    protected AbstractTrie<T> getUniqueBranch(String keyword, int index, AbstractTrie<T> branchBase) throws NoAssociatedObjectsException {
        Character workingChar = keyword.charAt(index);
        if (childCharacters.containsKey(workingChar)) {
            if (index == keyword.length()-1) {
                return branchBase;
            }
            TrieNode<T> child = childCharacters.get(workingChar);
            return child.getUniqueBranch(keyword, index+1, branchBase);
        }
        else throw new NoAssociatedObjectsException();
    }
}
