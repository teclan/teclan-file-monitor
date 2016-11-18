package teclan.monitor.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class ExecutorServiceProvider implements Provider<ExecutorService> {

    @Inject
    @Named("config.thread.max")
    private int count;

    public ExecutorService get() {

        ExecutorService executorService = Executors.newFixedThreadPool(count);

        // NOTE
        // if you want to get the size info of the ExecutorService,please cast
        // to ThreadPoolExecutor,like this:
        // ((ThreadPoolExecutor) executorService).getMaximumPoolSize();

        return executorService;
    }

}
