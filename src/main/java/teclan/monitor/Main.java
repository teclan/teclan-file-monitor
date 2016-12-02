package teclan.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import teclan.guice.Module.ConfigModule;
import teclan.monitor.file.FileCheck;
import teclan.monitor.module.MonitorModule;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Inject
    private FileCheck           fileCheck;

    public void start() {

        fileCheck.init();

        fileCheck.check();

    }

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(
                new ConfigModule("config.conf", "config"), new MonitorModule());

        Main monitor = injector.getInstance(Main.class);
        monitor.start();
    }
}
