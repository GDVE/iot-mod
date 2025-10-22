package ru.iot.api.persistence.hibernate;

import lombok.Getter;
import lombok.Setter;
import ru.iot.api.config.GsonConfig;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class HibernateDataSourceConfig extends GsonConfig {

    private String jdbcUrl = "jdbc:postgresql://localhost:5432/iotmod";
    private String username = "postgres";
    private String password = "postgres";
    private int maxPoolSize = 10;
    private Map<String, Object> hibernateProperties = new HashMap<>();

    public HibernateDataSourceConfig(Path path) {
        super(path);
        hibernateProperties.put("hibernate.show_sql", "false");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.transaction.jta.platform", "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform");
    }
}
