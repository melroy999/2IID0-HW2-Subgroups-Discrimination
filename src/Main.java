import instance.ArffFile;
import reader.ArffReader;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
