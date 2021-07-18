package ml.chlorinelabs.razoid.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class DirectStreamReader {

    private File file;

    public DirectStreamReader(String st) {
        file = new File(st);
    }

    public DirectStreamReader(File file) {
        this.file = file;
    }

    public Vector<String> read() throws IOException {
        Vector<String> vector = new Vector<>(1,1);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(true) {
            String st = br.readLine();
            if(st == null) break;
            vector.addElement(st);
        }
        br.close();
        return vector;
    }
}
