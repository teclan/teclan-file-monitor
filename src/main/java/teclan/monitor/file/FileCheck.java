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
import teclan.monitor.file.listener.FileChangeListener;
import teclan.utils.FileUtils;

@Singleton
public class FileCheck {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCheck.class);

	@Inject
	@Named("config.file.monitor-dir")
	private String monitorDir;
	@Inject
	@Named("config.file.backup-dir")
	private String backupDir;

	@Inject
	private Database database;
	@Inject
	private FileChangeListener fileChangeListener;

	public void init() {
		File file = new File(backupDir);
		database.openDatabase();
		scanDir(file);
		LOGGER.info("目录 {} 初始化结束", backupDir);
		database.closeDatabase();

	}

	public void check() {
		database.openDatabase();
		List<FileRecords> records = FileRecords.findAll();

		for (FileRecords record : records) {
			File file = new File(record.getString("path"));

			if (!file.exists()) {
				LOGGER.warn("File was deleted !!! :{}", file.getAbsoluteFile());
				fileChangeListener.recover(record.getString("backup_path"), file.getAbsolutePath());
				continue;
			}

			if (file.lastModified() != record.getLong("last_modified") || file.length() != record.getLong("length")
					|| !FileUtils.getFileSummary(file.getAbsoluteFile(), "SHA-256")
							.equals(record.getString("summary"))) {
				fileChangeListener.recover(record.getString("backup_path"), file.getAbsolutePath());
				LOGGER.warn("File was modified !!! :{}", file.getAbsoluteFile());
			}
		}
		database.closeDatabase();
		

		try {
			Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		check();
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

		try {

			FileRecords record = FileRecords.findFirst("backup_path = ?", file.getAbsolutePath());

			if (record != null) {
				LOGGER.warn("record already exists,{}", file.getAbsolutePath());
				return;
			}

			record = (FileRecords) FileRecords.getMetaModel().getModelClass().newInstance();

			String summary = FileUtils.getFileSummary(file.getAbsoluteFile(), "SHA-256");

			record.set("backup_path", file.getAbsolutePath(),"path",file.getAbsolutePath().replace(backupDir,monitorDir) ,"length", length, "last_modified", lastModified, "summary",
					summary).saveIt();

		} catch (InstantiationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void show(){
		
		database.openDatabase();
		
		List<FileRecords> records = FileRecords.findAll();
		
		for(FileRecords record:records){
			LOGGER.info("{}",record);
		}
		database.closeDatabase();
		
	}

}
