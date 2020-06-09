package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Controller{
    @FXML
    TextField input;
    @FXML
    TextField output;
    @FXML
    TextField outputFilename;
    @FXML
    Button readInput;
    @FXML
    Button clearButton;
    @FXML
    Button compress;
    @FXML
    Button decompress;
    @FXML
    TableView huffmanTable = new TableView();
    @FXML
    TableColumn<Map, String> descriptorsCol = new TableColumn<>();
    @FXML
    TableColumn<Map, String> huffmanCodeCol = new TableColumn<>();

    String mapKey = "huffmanCode";
    String mapValue = "descriptor";


    public void enableButtons(){
        if(input.getText().isEmpty()){
            compress.setDisable(true);
            decompress.setDisable(true);
        }
        else{
            if(Standard_Huffman.huffmanCodes.isEmpty())
                compress.setDisable(false);
            else {
                fillTable();
                decompress.setDisable(false);
            }
        }
    }


    public void readFromFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("E:\\FCI\\third year\\Multimedia\\JPEG_Huffman_RLE"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(Main.stage);
        try {
            Scanner scanner = new Scanner(selectedFile);
            input.setText(scanner.nextLine());
            if(scanner.hasNextLine())
                Main.nZero = scanner.nextInt();
            while (scanner.hasNextLine()){
                Standard_Huffman.huffmanCodes.put(scanner.next(), scanner.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        enableButtons();
    }


    public ObservableList<Map> fillObservableList(){
        ObservableList<Map> tableData = FXCollections.observableArrayList();
        for(String code : Standard_Huffman.huffmanCodes.keySet()){
            Map<String, String> row = new HashMap<>();
            row.put(mapKey, code);
            row.put(mapValue, Standard_Huffman.huffmanCodes.get(code));
            tableData.add(row);
        }
        return tableData;
    }


    public void fillTable(){
        huffmanCodeCol.setCellValueFactory(new MapValueFactory<>(mapKey));
        descriptorsCol.setCellValueFactory(new MapValueFactory<>(mapValue));
        huffmanTable.getColumns().setAll(descriptorsCol, huffmanCodeCol);
        huffmanTable.setItems(fillObservableList());
    }


    public void onCompression(){
        Main.inputStream = input.getText();
        Main.compression();
        output.setText(Main.outputStream);
        fillTable();
        if(!outputFilename.getText().isEmpty())
            writeInFile(true);
    }


    public void onDecompression(){
        Main.inputStream = input.getText();
        Main.decompression();
        output.setText(Main.outputStream);
        if(!outputFilename.getText().isEmpty())
            writeInFile(false);
    }


    public void writeInFile(boolean writeTable){
        File outFile = new File(outputFilename.getText());
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            StringBuilder output = new StringBuilder();
            output.append(Main.outputStream);
            if(writeTable){
                output.append("\n");
                output.append(Main.nZero);
                for (String code : Standard_Huffman.huffmanCodes.keySet()){
                    output.append("\n");
                    output.append(code);
                    output.append(" ");
                    output.append(Standard_Huffman.huffmanCodes.get(code));
                }
            }
            fos.write(output.toString().getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    public void clear(){
        input.clear();
        output.clear();
        outputFilename.clear();
        huffmanTable.getItems().clear();
        compress.setDisable(true);
        decompress.setDisable(true);
        Main.inputStream = "";
        Main.outputStream = "";
        Main.descriptors.clear();
        Main.groups.clear();
        Standard_Huffman.huffmanCodes.clear();
        Standard_Huffman.root = new Node(null, 0);
    }
}
