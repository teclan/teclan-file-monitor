package teclan.monitor.config;

import java.util.List;
import java.util.Map.Entry;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

public class ConfigBinder {
    private Binder binder;

    public ConfigBinder(Binder binder) {
        this.binder = binder;
    }

    public void bind(Config config, String root) {
        bind(config, root, "config");
    }

    public void bind(Config config, String root, String bindingPath) {
        bind(config.getObject(root), bindingPath);
    }

    @SuppressWarnings("rawtypes")
    private void bind(ConfigValue configValue, String bindingPath) {
        switch (configValue.valueType()) {
        case OBJECT:
            ConfigObject obj = (ConfigObject) configValue;
            binder.bind(Key.get(Config.class, Names.named(bindingPath)))
                    .toInstance(obj.toConfig());
            for (Entry<String, ConfigValue> e : obj.entrySet()) {
                bind(e.getValue(), bindingPath + "." + e.getKey());
            }
            break;
        case LIST:
            ConfigList values = (ConfigList) configValue;
            for (int i = 0; i < values.size(); i++) {
                bind(values.get(i), bindingPath + "." + i);
            }

            // WARN
            // Don't try to change the List to List<?>, it will produce werid
            // issues.
            binder.bind(List.class).annotatedWith(Names.named(bindingPath))
                    .toInstance((List) configValue.unwrapped());
            break;
        case NUMBER:
            binder.bindConstant().annotatedWith(Names.named(bindingPath))
                    .to((Integer) configValue.unwrapped());
            break;
        case BOOLEAN:
            binder.bindConstant().annotatedWith(Names.named(bindingPath))
                    .to((Boolean) configValue.unwrapped());
            break;
        case NULL:
            break;
        case STRING:
            binder.bindConstant().annotatedWith(Names.named(bindingPath))
                    .to(configValue.unwrapped().toString());
        }
    }

}
