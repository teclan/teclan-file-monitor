package teclan.monitor.db;

import java.util.HashMap;
import java.util.Map;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.javalite.activejdbc.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static final Logger                  LOGGER       = LoggerFactory
            .getLogger(Database.class);

    // 连接名
    private String                               name;
    private DataSource                           dataSource   = null;

    // 连接池
    private static Map<String, HikariDataSource> DATA_SOURCES = new HashMap<String, HikariDataSource>();

    public Database(DataSource dataSource) {
        this("default", dataSource);
    }

    public Database(String name, DataSource dataSource) {
        this.name = name;
        this.dataSource = dataSource;
    }

    public void initDb() {
        this.initDb(dataSource, false, false);
    }

    public void initDb(boolean migrate) {
        this.initDb(dataSource, migrate, false);
    }

    public void initDb(boolean migrate, boolean migrateClean) {
        this.initDb(dataSource, migrate, migrateClean);
    }

    private void initDb(DataSource dataSource, boolean migrate,
            boolean migrateClean) {

        this.dataSource = dataSource;

        String key = generateKeyForPool(dataSource);

        HikariDataSource hikariDataSource = generateHikariDataSource(
                dataSource);

        if (!DATA_SOURCES.containsKey(key)) {
            DATA_SOURCES.put(key, hikariDataSource);
        }

        if (migrate) {
            Flyway flyway = new Flyway();
            flyway.setDataSource(hikariDataSource);

            if (migrateClean) {
                flyway.clean();
            }

            try {
                flyway.migrate();
            } catch (FlywayException e) {
                LOGGER.error(
                        "数据库迁移检验失败,表结构可能已经修改,建议迁移之前将 migrateClean 设为 true,如果是生产环境请联系DBA确认后重试 !");
                throw e;
            }
        } else {
            LOGGER.warn("跳过数据迁移!");
        }
    }

    private String generateKeyForPool(DataSource dataSource) {
        StringBuffer appender = new StringBuffer();

        appender.append(dataSource.getType());
        appender.append(dataSource.getHost());
        appender.append(dataSource.getPort());
        appender.append(dataSource.getDbName());
        appender.append(dataSource.getSchema());
        appender.append(dataSource.getUser());

        return appender.toString();
    }

    private HikariDataSource generateHikariDataSource(DataSource dataSource) {

        HikariConfig config = new HikariConfig();

        config.setDriverClassName(dataSource.getDriverClass());
        config.setJdbcUrl(dataSource.getUrl());
        config.setUsername(dataSource.getUser());
        config.setPassword(dataSource.getPassword());

        return new HikariDataSource(config);
    }

    public boolean openDatabase() {
        try {
            new DB(name).open(DATA_SOURCES.get(generateKeyForPool(dataSource)));
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public void closeDatabase() {
        new DB(name).close();
    }

    public boolean openDatabase(String name) {
        try {
            new DB(name).open(DATA_SOURCES.get(generateKeyForPool(dataSource)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void closeDatabase(String name) {
        new DB(name).close();
    }
}
