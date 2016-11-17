package teclan.monitor.module;

import java.util.concurrent.ExecutorService;

import com.google.inject.AbstractModule;

import teclan.monitor.db.Database;
import teclan.monitor.provider.DatabaseProvider;
import teclan.monitor.provider.ExecutorServiceProvider;

public class TeclanModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Database.class).toProvider(DatabaseProvider.class);
        bind(ExecutorService.class).toProvider(ExecutorServiceProvider.class);

    }

}
