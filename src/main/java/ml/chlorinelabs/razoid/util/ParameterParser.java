package ml.chlorinelabs.razoid.util;

import java.util.Vector;

public class ParameterParser {

    private String[] args;

    private Vector<String> flags;

    private Vector<String> parametricFlags;

    public ParameterParser(String[] a) {
        args = a;
        parse();
    }

    private void parse() {
        for(int i = 0; i < args.length - 1; i++) {
            if(args[i].startsWith("--") && args[i+1].startsWith("--")) {
                flags.addElement(args[i]);
                args[i] = "";
            }
        }
        String parameter = "";
        for(String st:args) {
            if(st.startsWith("--")) {
                parametricFlags.addElement(parameter);
                parameter = "";
            } else if(st.equals("")) {
                continue;
            } else {
                parameter += st;
            }
        }

    }

}
