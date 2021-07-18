package ml.chlorinelabs.razoid.util;

import java.util.List;
import java.util.concurrent.*;

class ProcessingController {

    private static final int THREADS_PER_CORE = 2;

    private static final long POLLING_RATE = 480;

    private static ExecutorService executorService;

    private static ScheduledExecutorService processorPollingService;

    private static int processorCount;

    private static int threadCount = THREADS_PER_CORE * processorCount;

    private static boolean isScheduledPollingEnabled = true;

    private static void initializePolling() {
        processorPollingService = Executors.newSingleThreadScheduledExecutor();
        Callable<Void> pollTask = () -> {
            pollProcessorCount();
            return null;
        };
        processorPollingService.schedule(pollTask, POLLING_RATE, TimeUnit.SECONDS);
    }

    public static void initialize() {
        if(isScheduledPollingEnabled) initializePolling();
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public static void execute(List<Callable<Void>> tasks) throws InterruptedException {
        executorService.invokeAll(tasks);
    }

    public static int getProcessorCount() {
        return processorCount;
    }

    public static int getThreadCount() {
        return threadCount;
    }

    public static void pollProcessorCount() {
        processorCount = Runtime.getRuntime().availableProcessors();
        threadCount = processorCount * THREADS_PER_CORE;
    }

    public static void disableScheduledProcessorPolling() {
        isScheduledPollingEnabled = false;
    }

    public static boolean isInitialized() { return (executorService != null); }

}
