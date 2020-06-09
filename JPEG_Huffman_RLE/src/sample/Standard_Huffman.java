package sample;

import java.util.*;

public class Standard_Huffman {
    public static TreeMap<String, String> huffmanCodes = new TreeMap<>();
    public static ArrayList<Integer> frequencies = new ArrayList<>();
    public static ArrayList<String> symbols = new ArrayList<>();
    public static Node root = new Node(null, 0);


    public static void huffmanCode(Node node, String code) {
        if (node == null)
            return;
        if (node.left == null && node.right == null)
            huffmanCodes.put(code, node.symbol);
        huffmanCode(node.left, code + "1");
        huffmanCode(node.right, code + "0");
    }

    public static void buildHuffmanTree(){

        ArrayList<Node> nodes = new ArrayList<>();
        while (frequencies.size() != 0){
            int index = frequencies.indexOf(Collections.min(frequencies));
            nodes.add(new Node(symbols.get(index), frequencies.get(index)));
            symbols.remove(index);
            frequencies.remove(index);
        }

        while (nodes.size() > 1){
            Node left = nodes.remove(0);
            Node right = nodes.remove(0);
            Node combine = new Node(null, left.freq + right.freq, left, right);
            root = combine;
            int index = 0;
            for (int i = 0; i < nodes.size(); i++) {
                if(combine.freq > nodes.get(i).freq)
                    index++;
            }
            nodes.add(index,combine);
        }
        huffmanCode(root,"");
    }

    public static String getCode(String descriptor){
        for(String code : huffmanCodes.keySet()){
            if(descriptor.equals(huffmanCodes.get(code)))
                return code;
        }
        return null;
    }
}
