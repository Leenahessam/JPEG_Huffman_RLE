package sample;

public class Node {
    String symbol;
    int freq;
    Node left, right;

    Node(String symbol, int freq) {
        this.symbol = symbol;
        this.freq = freq;
        this.left = null;
        this.right = null;
    }

    public Node(String symbol, int freq, Node left, Node right) {
        this.symbol = symbol;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}
