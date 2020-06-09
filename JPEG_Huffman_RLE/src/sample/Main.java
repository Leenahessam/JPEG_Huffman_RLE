package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

public class Main extends Application {

    public static String inputStream = new String();
    public static String outputStream = new String();
    public static ArrayList<ArrayList<String>> groups = new ArrayList<>();
    public static ArrayList<String> descriptors = new ArrayList<>();
    public static int nZero;
    public static Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("JPEG");
        primaryStage.setScene(new Scene(root, 750, 500));
        stage = primaryStage;
        primaryStage.show();
    }

    public static void splitInput() {

        String[] splitted = inputStream.split(",");
        ArrayList<String> input = new ArrayList<>();
        input.addAll(Arrays.asList(splitted));
        while (input.size() != 64)
            input.add("0");
        ArrayList<String>group = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            int value = Integer.parseInt(input.get(i));
            group.add(input.get(i));
            if(value != 0) {
                ArrayList<String> temp = new ArrayList<>();
                temp.addAll(group);
                groups.add(temp);
                group.clear();
            }
            if(i == input.size()-1){
                nZero = group.size();
                group.clear();
                group.add("EOB");
                groups.add(group);
            }
        }
    }

    public static int getCategory(int value){
        return (int) (Math.log(Math.abs(value)) / Math.log(2) + 1);
    }

    public static void getDescriptors(){
        for (ArrayList<String> group : groups) {
            StringBuilder stringBuilder = new StringBuilder();
            if(group.contains("EOB"))
                stringBuilder.append("EOB");
            else{
                stringBuilder.append(group.size()-1);
                stringBuilder.append("/");
                int category = getCategory(Integer.parseInt(group.get(group.size()-1)));
                stringBuilder.append(category);
            }
            descriptors.add(stringBuilder.toString());
        }
    }

    public static void getFrequencies(){
        for (String descriptor : descriptors) {
            if(!Standard_Huffman.symbols.contains(descriptor)){
                Standard_Huffman.symbols.add(descriptor);
                Standard_Huffman.frequencies.add(Collections.frequency(descriptors, descriptor));
            }
        }
    }

    public static String getAdditionalBits(int value){
        String binary = Integer.toBinaryString(Math.abs(value));
        if(value>0)
            return binary;
        else{
            String inv = "";
            for (int i = 0; i < binary.length(); i++) {
                if(binary.charAt(i) == '0')
                    inv += '1';
                else
                    inv += '0';
            }
            return inv;
        }
    }

    public static int getNumber(String binary){
        if(binary.charAt(0) == '1'){
            return Integer.parseInt(binary, 2);
        }
        else{
            String inv = "";
            for (int i = 0; i < binary.length(); i++) {
                if(binary.charAt(i) == '0')
                    inv += '1';
                else
                    inv += '0';
            }
            return (Integer.parseInt(inv, 2) * -1);
        }
    }

    public static void compression(){
        splitInput();
        getDescriptors();
        getFrequencies();
        Standard_Huffman.buildHuffmanTree();
        for (int i = 0; i < descriptors.size(); i++) {
            outputStream += Standard_Huffman.getCode(descriptors.get(i));
            if(!descriptors.get(i).equals("EOB"))
                outputStream += getAdditionalBits(Integer.parseInt(groups.get(i).get(groups.get(i).size()-1)));
        }
    }

    public static void decompression(){
        for (int i = 0; i < inputStream.length(); i++) {
            String code = "";
            while (!Standard_Huffman.huffmanCodes.containsKey(code)){
                code += inputStream.charAt(i);
                i++;
            }
            if(Standard_Huffman.huffmanCodes.get(code).equals("EOB")) {
                for (int j = 0; j < nZero; j++)
                    outputStream += "0,";
                break;
            }
            String[] descriptor = Standard_Huffman.huffmanCodes.get(code).split("/");
            int nZeroes = Integer.parseInt(descriptor[0]);
            int nBits = Integer.parseInt(descriptor[1]);
            for (int j = 0; j < nZeroes; j++)
                outputStream += "0,";
            code = inputStream.substring(i, i+nBits);
            outputStream += (getNumber(code) + ",");
            i += (nBits-1);
        }
        outputStream = outputStream.substring(0, outputStream.length()-1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
