package ru.iot.api.persistence.hibernate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import lombok.Getter;
import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.bytecode.spi.ClassTransformer;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import ru.iot.api.persistence.DataSource;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Getter
public abstract class HibernateDataSource implements DataSource {

    private final HikariDataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;
    private final Properties hibernateProperties;

    public HibernateDataSource(HibernateDataSourceConfig config) {
        dataSource = buildDataSource(config);
        hibernateProperties = buildHibernateProperties(config);
        entityManagerFactory = buildEntityManagerFactory(config);
    }

    private HikariDataSource buildDataSource(HibernateDataSourceConfig config) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getJdbcUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        return new HikariDataSource(hikariConfig);
    }

    private Properties buildHibernateProperties(HibernateDataSourceConfig config) {
        var properties = new Properties();
        properties.putAll(config.getHibernateProperties());
        return properties;
    }

    private EntityManagerFactory buildEntityManagerFactory(HibernateDataSourceConfig config) {
        return new EntityManagerFactoryBuilderImpl(
                this,
                config.getHibernateProperties()
        ).build();
    }

    @Override
    public void close() {
        DataSource.super.close();
        dataSource.close();
    }

    @Override
    public String getProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public boolean isUseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean isExcludeUnlistedClasses() {
        return false;
    }

    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.ENABLE_SELECTIVE;
    }

    @Override
    public List<String> getMappingFileNames() {
        return Collections.emptyList();
    }

    @Override
    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public Object getNonJtaDataSource() {
        return dataSource;
    }

    @Override
    public Object getJtaDataSource() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return hibernateProperties;
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public ClassLoader getTempClassLoader() {
        return null;
    }

    @Override
    public void pushClassTransformer(EnhancementContext enhancementContext) {
    }

    @Override
    public ClassTransformer getClassTransformer() {
        return null;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }
}
