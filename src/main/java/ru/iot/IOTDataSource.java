package ru.iot;

import ru.iot.api.persistence.RepositoryException;
import ru.iot.api.persistence.hibernate.HibernateDataSourceConfig;
import ru.iot.api.persistence.hibernate.HibernateDataSource;
import ru.iot.message.MessageEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IOTDataSource extends HibernateDataSource {

    public IOTDataSource() {
        super(initConfig());
    }

    private static HibernateDataSourceConfig initConfig() {
        Path path = Paths.get(System.getProperty("user.dir") + "/config/iot_datasource.json");
        HibernateDataSourceConfig config = new HibernateDataSourceConfig(path);
        try {
            config.readFromDisk();
        } catch (IOException e) {
            throw new RepositoryException("Failed to initialize datasource config!", e);
        }
        return config;
    }

    @Override
    public String getName() {
        return IOTMod.MOD_ID;
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of(MessageEntity.class.getName());
    }
}
