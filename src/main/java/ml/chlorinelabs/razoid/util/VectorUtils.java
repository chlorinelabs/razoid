package ml.chlorinelabs.razoid.util;

import java.util.Vector;

public class VectorUtils {

    public static String[] convertVector(Vector<String> vector) {
        String[] array = new String[vector.size()];
        for(int i=0;i<vector.size();i++) array[i] = vector.elementAt(i);
        return array;
    }

    public static void convertVector(Vector<String> vector, String[] array) {
        for(int i=0;i<vector.size();i++) array[i] = vector.elementAt(i);
    }

    public static <T> void convertVector(Vector<T> vector, T[] array) {
        for(int i=0;i<vector.size();i++) array[i] = vector.elementAt(i);
    }
}
