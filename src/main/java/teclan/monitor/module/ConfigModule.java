package teclan.monitor.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import teclan.monitor.config.ConfigBinder;

public class ConfigModule extends AbstractModule {
    private final Logger LOGGER = LoggerFactory.getLogger(ConfigModule.class);
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
