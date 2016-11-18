package teclan.monitor.module;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import teclan.guice.config.ConfigBinder;

public class ConfigModule extends AbstractModule {
    private final String configFile;
    private final String root;

    public ConfigModule(String configFile, String root) {
        this.configFile = configFile;
        this.root = root;
    }

    @Override
    protected void configure() {

        Config config = ConfigFactory.load(this.configFile);

        new ConfigBinder(binder()).bind(config, this.root);
    }
}
