package solution;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.spockframework.runtime.extension.IGlobalExtension;
import org.spockframework.runtime.model.SpecInfo;

public class ExecutionLogExtension implements IGlobalExtension {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.nnn");

    private final ConcurrentLinkedQueue<String> log = new ConcurrentLinkedQueue<>();

    private final ExecutionLogInterceptor interceptor = new ExecutionLogInterceptor(log);

    private ExecutionLogConfig config;

    private boolean written = false;

    @Override
    public void start() {
        // do nothing
    }

    @Override
    public void visitSpec(SpecInfo spec) {
        if (config.enabled) {
            spec.addInterceptor(interceptor);
            spec.getAllFeatures().forEach(featureInfo -> featureInfo.addIterationInterceptor(interceptor));
        }
    }

    @Override
    public void stop() {
        if (!log.isEmpty() && !written) {
            written = true;
            File target = new File("target", "execution-" + DATE_TIME_FORMATTER.format(LocalDateTime.now()) + ".log");
            try (PrintWriter pw = new PrintWriter(new FileWriter(target))) {
                log.forEach(pw::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
