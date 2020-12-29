import org.apache.commons.lang3.time.StopWatch;

public class AutoStopWatch implements AutoCloseable {
    private final String name;
    private final StopWatch watch;

    public AutoStopWatch(String name) {
        this.name = name;
        this.watch = new StopWatch();
        watch.start();
    }

    @Override
    public void close() {
        watch.stop();
        System.out.printf("%s - elapsed time: %sms\n", name, watch.getTime());
    }
}
