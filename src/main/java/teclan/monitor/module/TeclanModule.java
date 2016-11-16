package teclan.monitor.module;

import com.google.inject.AbstractModule;

import teclan.monitor.db.Database;
import teclan.monitor.provider.DatabaseProvider;

public class TeclanModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Database.class).toProvider(DatabaseProvider.class);

    }

}
