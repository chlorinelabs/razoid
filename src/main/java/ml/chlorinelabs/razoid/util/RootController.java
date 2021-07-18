package ml.chlorinelabs.razoid.util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

public class RootController {

    private final Vector<LogSystem> logSystemVector;
    private final Vector<DataRecorder> dataRecorderVector;
    private final static String[] rootAssociates = {
            "RootExtensions"
    };
    private final static Scanner sc = new Scanner(System.in);

    private static List<Callable<Void>> executionStack;

    public RootController(boolean verbose) {
        // LogSystem Vector Initialization
        logSystemVector = new Vector<>(1,1);

        // Primary LogSystem Initialization
        try {
            logSystemVector.addElement(new LogSystem("rootcontroller.log", "ROOT",verbose));
            // LogSystem Initial Messages
            getPrimaryLogSystem().init();
            primaryLog("LogSystem Initialization Complete (Stepping RZ101) ...");
        } catch (IOException ioe) {
            System.out.println("ERROR (FATAL): RootController Initialization Failed! Error Code RZ101 ... Terminating Program.");
            System.out.println("ERROR (FATAL): Dumping Exception StackTrace ... ");
            ioe.printStackTrace();
            rootSystemExit(101);
        }

        // DataRecorder Initialization
        dataRecorderVector = new Vector<>(1,1);

        // Enable Multiprocessor Job Allocation
        ProcessingController.pollProcessorCount();
        ProcessingController.disableScheduledProcessorPolling();
        ProcessingController.initialize();
        primaryLog("ProcessingController now Initialized ("+ProcessingController.getProcessorCount()+" cores, "+ProcessingController.getThreadCount()+" threads)");

        // Initialize Processor Execution Stack
        executionStack = new ArrayList<>();
        primaryLog("ProcessorExecutionStack Initialized");
    }

    public int createLogSystem(String fn, String prefix, boolean verbose) {
        int id = logSystemVector.size();
        primaryLog("INFO: Received request creation of additional logsystem at ["+id+"] for \""+fn+"\" with verbosity '"+verbose+"' ...");
        try {
            logSystemVector.addElement(new LogSystem(fn, prefix, verbose));
            logSystemVector.elementAt(id).init();
        } catch (IOException ioe) {
            primaryLog("WARN: Creation of additional logsystem failed (Error Code RZ201). Severity: High. Continuing execution ... ");
            primaryLog("WARN: Dumping Exception StackTrace ... ");
            primaryLog(ioe.getMessage());
        }
        return id;
    }

    public int createDataRecorder(String fn) {
        int id = dataRecorderVector.size();
        primaryLog("INFO: Received request creation of additional data recorder at ["+id+"] for \""+fn+"\"");
        try {
            dataRecorderVector.addElement(new DataRecorder(fn));
        } catch (IOException ioe) {
            primaryLog("WARN: Creation of additional data recorder failed (Error Code RZ203). Severity: High. Continuing execution ... ");
            primaryLog("WARN: Dumping Exception StackTrace ... ");
            primaryLog(ioe.getMessage());
        }
        return id;
    }

    public LogSystem getLogSystem(int id) {
        primaryLog("INFO: Received request for accessing logsystem at ["+id+"] ...");
        if(id == 0)  {
            primaryLog("ERROR (FATAL): Request for accessing logsystem [0] declined. RootController logs are non-accessible except by RootInternals. (Error Code: RZ301) Terminating Program ...");
            rootSystemExit(301);
        }
        if(id >= logSystemVector.size()) {
            primaryLog("ERROR (FATAL): Request for accessing logsystem ["+id+"] declined. RootLogSystemVector does not have the required logsystem (Error Code: RZ302) Terminating Program ...");
            rootSystemExit(302);
        }
        if(id < 0) {
            primaryLog("ERROR (FATAL): Request for accessing logsystem ["+id+"] declined. Request has negative pointer index (Error Code: RZ303) Terminating Program ...");
            rootSystemExit(303);
        }
        try {
            return logSystemVector.elementAt(id);
        } catch (Exception e) {
            primaryLog("ERROR (FATAL): Request for accessing logsystem ["+id+"] cannot be processed. Request was met with an unknown error while processing (Error Code: RZ304) Terminating Program ...");
            rootSystemExit(304);
        }
        return null;
    }

    public DataRecorder getDataRecorder(int id) {
        primaryLog("INFO: Received request for accessing data recorder at ["+id+"] ...");
        if(id >= dataRecorderVector.size()) {
            primaryLog("ERROR (FATAL): Request for accessing data recorder ["+id+"] declined. RootDataRecorderVector does not have the required data recorder (Error Code: RZ306) Terminating Program ...");
            rootSystemExit(306);
        }
        if(id < 0) {
            primaryLog("ERROR (FATAL): Request for accessing data recorder ["+id+"] declined. Request has negative pointer index (Error Code: RZ307) Terminating Program ...");
            rootSystemExit(307);
        }
        try {
            return dataRecorderVector.elementAt(id);
        } catch (Exception e) {
            primaryLog("ERROR (FATAL): Request for accessing data recorder ["+id+"] cannot be processed. Request was met with an unknown error while processing (Error Code: RZ308) Terminating Program ...");
            rootSystemExit(308);
        }
        return null;
    }

    public void log(int id, String msg) {
        try {
            getLogSystem(id).log(msg);
        } catch (Exception e) {
            primaryLog("WARN: Request for logging a message into logsystem ["+id+"] cannot be processed. Request was met with an unknown error while processing (Error Code: RZ202, Severity: Low)  Continuing Execution ...");
        }
    }

    public void datarecord(int id, String data) {
        try {
            getDataRecorder(id).record(data);
        } catch (Exception e) {
            primaryLog("WARN: Request for recording data into data recorder["+id+"] cannot be processed. Request was met with an unknown error while processing (Error Code: RZ204, Severity: Low)  Continuing Execution ...");
        }
    }

    public static void requestTermination(String carrier, int severity, int errorCode, String request) {
        System.out.println("ROOT: Request for Termination of GlobalRootController Received. (RZ305)");
        System.out.println("ROOT: Request Details:");
        System.out.println("ROOT: Request Carrier Qualified Name: "+carrier);
        System.out.println("ROOT: Request Carrier Severity      : "+severity);
        System.out.println("ROOT: Request Carrier Error Code    : "+errorCode);
        System.out.println("ROOT: Request Carrier Message       : "+request);
        System.out.println("ROOT: Is Qualified Root Associate   : "+isAssociate(carrier));
        System.out.print("ROOT: Do you want to allow for termination (y/N): ");
        String st = sc.nextLine().trim().toLowerCase(Locale.ROOT);
        if(st.charAt(0) == 'y') {
            System.out.println("ROOT: Termination Request Accepted");
            System.exit(errorCode);
        } else if(st.charAt(0) == 'n') {
            System.out.println("ROOT: Termination Request Declined");
        } else {
            System.out.println("ROOT: Termination Request Declined");
        }
    }

    public static String getUserInput() {
        return sc.nextLine();
    }

    public void clearExecutionStack() {
        executionStack.clear();
        primaryLog("Request to Clear ExecutionStack Accepted ...");
    }

    public void addToExecutionStack(Callable<Void> callable) {
        executionStack.add(callable);
        primaryLog("Request to Add New Callable to ExecutionStack Accepted ...");
    }

    public void executeStack() {
        primaryLog("ExecutionStack sent to ProcessingController ...");
        try {
            ProcessingController.execute(executionStack);
        } catch (InterruptedException ie) {
            primaryLog("ERROR: ExecutionStack from ProcessingController was halted. Unknown InterruptedException walked in. (Error Code RZ205). Terminating ...");
            System.out.println("ERROR: ExecutionStack from ProcessingController was halted. Unknown InterruptedException walked in. (Error Code RZ205). Terminating ...");
            rootSystemExit(205);
        }
    }

    private LogSystem getPrimaryLogSystem() {
        return logSystemVector.firstElement();
    }

    private static boolean isAssociate(String carrier) {
        for(String st:rootAssociates) if(st.equals(carrier)) return true;
        return false;
    }

    private void primaryLog(String msg) {
        try {
            getPrimaryLogSystem().log(msg);
        } catch (IOException ioe) {
            System.out.println("ERROR (FATAL): RootController Logging Failed! Error Code RZ102 ... Terminating Program.");
            System.out.println("ERROR (FATAL): Dumping Exception StackTrace ... ");
            ioe.printStackTrace();
            rootSystemExit(102);
        }
    }

    private void rootSystemExit(int code) {
        // TODO Some dumping
        System.exit(code);
    }

}
