import ml.chlorinelabs.razoid.util.RootController;
import ml.chlorinelabs.razoid.util.RootExtensions;

public class App extends RootExtensions {
    // Testing, :)

    public static void main(String[] args) {
        RootController rc = new RootController(false);
        registerRootController(rc);
        int i = createLogSystem("test", "TEST",true);
        log(i, null);
    }

}
