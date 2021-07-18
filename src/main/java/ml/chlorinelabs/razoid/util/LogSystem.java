package ml.chlorinelabs.razoid.util;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class LogSystem {

    private final String file;
    private final String pfx;
    private final boolean ps;
    private final DirectStreamWriter dswi;

    public LogSystem(String fn, String prefix, boolean printStream) throws IOException {
        file = fn;
        ps = printStream;
        dswi = new DirectStreamWriter(file);
        File f = new File(file);
        if(!f.exists()) f.createNewFile();
        else {
            f.delete();
            f.createNewFile();
        }
        pfx = prefix;
    }

    public void init() throws IOException {
        log("LogSystem: Logging started for file "+file);
    }

    public void log(String message) throws IOException {
        Vector<String> fc = (new DirectStreamReader(file)).read();
        fc.addElement(pfx+": "+message);
        dswi.write(fc);
        if(ps) System.out.println(pfx+": "+message);
    }

}
