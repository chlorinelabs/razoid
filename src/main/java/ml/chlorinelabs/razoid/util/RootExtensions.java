package ml.chlorinelabs.razoid.util;

import java.util.Locale;

public class RootExtensions {

    private static RootController rcint = null;

    protected static void registerRootController(RootController rc) {
        rcint = rc;
    }

    protected static int createLogSystem(String filename, String prefix, boolean verbose) {
        checkInitialization();
        checkIPNull(filename, "createLogSystem", "filename");
        checkIPNull(prefix, "createLogSystem", "prefix");
        return rcint.createLogSystem(filename, prefix, verbose);
    }

    protected static LogSystem getLogSystem(int id) {
        checkInitialization();
        return rcint.getLogSystem(id);
    }

    protected static void log(int id, String msg) {
        checkInitialization();
        checkIPNull(msg, "log", "message");
        rcint.log(id, msg);
    }

    protected static String getUserInput() {
        return RootController.getUserInput();
    }

    protected static int createDataRecorder(String filename) {
        checkInitialization();
        checkIPNull(filename, "createDataRecorder", "filename");
        return rcint.createDataRecorder(filename);
    }

    protected static DataRecorder getDataRecorder(int id) {
        checkInitialization();
        return rcint.getDataRecorder(id);
    }

    protected static void datarecord(int id, String data) {
        checkInitialization();
        rcint.datarecord(id, data);
    }

    private static void checkInitialization() {
        if(rcint == null) {
            System.out.println("ROOTEXT: ERROR (FATAL): RootController not registered for RootExtensions (Error Code RZ211). Terminating execution ... ");
            System.out.println("Commencing GlobalRootController Termination due to absence of registered RootControllers");
            RootController.requestTermination("RootExtensions", 100, 211, "Unregistered RootController");
        }
    }

    private static void checkIPNull(Object o, String fname, String pname) {
        if(o == null) {
            System.out.print("ROOTEXT: WARN: Null Parameter Detected (Error Code RZ212) [RootExtensions."+fname+"("+pname+")]. Proceed (Y/n) ? ");
            String st = RootController.getUserInput().toLowerCase(Locale.ROOT).trim();
            if(st.charAt(0) == 'y') {
                return;
            } else if(st.charAt(0) == 'n') {
                RootController.requestTermination("RootExtensions", 100, 212, "Null Parameter, User Requested Termination");
            } else {
                RootController.requestTermination("RootExtensions", 100, 212, "Null Parameter, User Requested Termination");
            }
        }
    }

    protected static void breakIFNull(Object o, String msg) {
        if(o == null) {
            System.out.print("ROOTEXT: WARN: Null Object Detected with On-Demand Check (Error Code RZ213) ["+msg+"]. Proceed (Y/n) ? ");
            String st = RootController.getUserInput().toLowerCase(Locale.ROOT).trim();
            if(st.charAt(0) == 'y') {
                return;
            } else if(st.charAt(0) == 'n') {
                RootController.requestTermination("RootExtensions", 100, 212, "Null Pointer, User Requested Termination");
            } else {
                RootController.requestTermination("RootExtensions", 100, 212, "Null Pointer, User Requested Termination");
            }
        }
    }

}
