package ru.iot.message;

import lombok.Getter;
import ru.iot.api.persistence.DataSource;
import ru.iot.api.persistence.Repository;
import ru.iot.api.persistence.RepositoryException;
import ru.iot.api.persistence.datasource.DataSourceHibernateImpl;
import ru.iot.api.persistence.datasource.DataSourceConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Getter
public class MessageRepo implements Repository<MessageEntity> {

    private final DataSource dataSource;

    public MessageRepo() {
        var config = new DataSourceConfig(Paths.get(System.getProperty("user.dir") + "/test.json"));
        try {
            config.readFromDisk();
        } catch (IOException e) {
            throw new RepositoryException(e);
        }

        dataSource = new DataSourceHibernateImpl(config) {
            @Override
            public String getName() {
                return "messages";
            }

            @Override
            public List<String> getManagedClassNames() {
                return List.of(MessageEntity.class.getName());
            }
        };
    }
}
