package ml.chlorinelabs.razoid.util;

import java.io.*;
import java.util.Vector;

public class DirectStreamWriter {

    private File file;

    public DirectStreamWriter(String st) {
        file = new File(st);
    }

    public DirectStreamWriter(File file) {
        this.file = file;
    }

    public void write(Vector<String> vector) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(String st:vector) bw.write(st+"\n");
        bw.close();
    }

}
