package teclan.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import teclan.monitor.file.FileCheck;
import teclan.monitor.module.ConfigModule;
import teclan.monitor.module.TeclanModule;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Inject
    private FileCheck           fileCheck;

    public void start() {
        fileCheck.init();

        fileCheck.show();

    }

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(
                new ConfigModule("config.conf", "config"), new TeclanModule());

        Main monitor = injector.getInstance(Main.class);
        monitor.start();
    }
}
