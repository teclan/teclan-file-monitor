package teclan.monitor.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import teclan.monitor.db.DataSource;
import teclan.monitor.db.Database;

@Singleton
public class DatabaseProvider implements Provider<Database> {

    @Inject
    @Named("config.db")
    private Config dbConfig;

    public Database get() {
        String url = String.format(dbConfig.getString("jdbc.url-template"),
                dbConfig.getString("jdbc.db-path"));
        DataSource dataSource = new DataSource(url,
                dbConfig.getString("jdbc.driver"),
                dbConfig.getString("jdbc.user"),
                dbConfig.getString("jdbc.password"));

        Database database = new Database(dbConfig.getString("name"),
                dataSource);

        database.initDb(dbConfig.getBoolean("migration.migrate"));

        return database;

    }

}
