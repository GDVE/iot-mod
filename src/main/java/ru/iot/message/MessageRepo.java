package ru.iot.message;

import lombok.Getter;
import ru.iot.IOTMod;
import ru.iot.api.persistence.DataSource;
import ru.iot.api.persistence.Repository;

@Getter
public class MessageRepo implements Repository<MessageEntity> {

    @Override
    public DataSource getDataSource() {
        return IOTMod.getInstance().getDataSource();
    }
}
