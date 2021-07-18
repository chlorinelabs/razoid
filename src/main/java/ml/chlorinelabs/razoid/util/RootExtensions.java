package ml.chlorinelabs.razoid.util;

import java.util.Locale;

public class RootExtensions {

    private static RootController rcint = null;

    protected static void registerRootController(RootController rc) {
        rcint = rc;
    }

    protected static int createLogSystem(String filename, String prefix, boolean verbose) {
        checkInitialization();
        checkNull(filename, "createLogSystem", "filename");
        checkNull(prefix, "createLogSystem", "prefix");
        return rcint.createLogSystem(filename, prefix, verbose);
    }

    protected static void getLogSystem(int id) {
        rcint.getLogSystem(id);
    }

    protected static void log(int id, String msg) {
        checkNull(msg, "log", "message");
        rcint.log(id, msg);
    }

    protected static String getUserInput() {
        return RootController.getUserInput();
    }

    private static void checkInitialization() {
        if(rcint == null) {
            System.out.println("ROOTEXT: ERROR (FATAL): RootController not registered for RootExtensions (Error Code RZ211). Terminating execution ... ");
            System.out.println("Commencing GlobalRootController Termination due to absence of registered RootControllers");
            RootController.requestTermination("RootExtensions", 100, 211, "Unregistered RootController");
        }
    }

    private static void checkNull(Object o, String fname, String pname) {
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

}
