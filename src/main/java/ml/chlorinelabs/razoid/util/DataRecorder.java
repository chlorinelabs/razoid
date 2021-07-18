package ml.chlorinelabs.razoid.util;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class DataRecorder {

    private File file;

    public DataRecorder(String fn) throws IOException {
        file = new File(fn);
        if(file.exists()) {
            file.delete();
            file.createNewFile();
        } else {
            file.createNewFile();
        }
    }

    public void record(String data) throws IOException {
        Vector<String> read = (new DirectStreamReader(file)).read();
        read.addElement(data);
        (new DirectStreamWriter(file)).write(read);
    }

}
