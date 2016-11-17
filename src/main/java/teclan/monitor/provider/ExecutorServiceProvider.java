package teclan.monitor.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import teclan.monitor.RecoverService;

@Singleton
public class ExecutorServiceProvider implements Provider<ExecutorService>{
	private final Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceProvider.class);
	
	@Inject
	 @Named("config.thread.max")
	private int count;

	public ExecutorService get() {
		
		ExecutorService executorService = Executors.newFixedThreadPool(count);
		
		LOGGER.info("count = {}",((ThreadPoolExecutor)executorService).getMaximumPoolSize());
		
		return  executorService;
	}

}
