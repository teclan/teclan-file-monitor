package teclan.monitor.file;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import teclan.monitor.db.Database;
import teclan.monitor.db.model.FileRecords;

@Singleton
public class FileCheck {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileCheck.class);

    @Inject
    @Named("config.file.monitor-dir")
    private String              monitorDir;

    @Inject
    private Database            database;

    public void init() {
        File file = new File(monitorDir);
        database.openDatabase();
        scanDir(file);
        LOGGER.info("目录 {} 初始化结束", monitorDir);
        database.closeDatabase();
    }

    private void scanDir(File file) {
        if (!file.exists()) {
            LOGGER.warn("\nthe file {} is not exists!", file.getAbsolutePath());
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                scanDir(files[i]);
            }
        } else {
            scanFile(file);
        }
    }

    private void scanFile(File file) {

        if (!file.exists()) {
            LOGGER.warn("file is not exists : {}", file.getAbsolutePath());
            return;
        }
        long lastModified = file.lastModified();
        long length = file.length();

        // TO DO
        // ADD check md5 etc.

        try {

            FileRecords record = FileRecords.findFirst("path = ?",
                    file.getAbsolutePath());

            if (record != null) {
                LOGGER.warn("record already exists,{}", file.getAbsolutePath());
                return;
            }

            record = (FileRecords) FileRecords.getMetaModel().getModelClass()
                    .newInstance();

            record.set("path", file.getAbsolutePath(), "length", length,
                    "last_modified", lastModified).saveIt();

        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void show() {

        database.openDatabase();

        List<FileRecords> fileRecords = FileRecords.findAll();

        for (FileRecords record : fileRecords) {
            LOGGER.info("=== {}", record);
        }

        database.closeDatabase();

    }
}
