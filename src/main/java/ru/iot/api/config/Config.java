package ru.iot.api.config;

import ru.iot.IOTMod;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Config {

    protected final transient Path path;

    protected Config(Path path) {
        this.path = path;
    }

    /**
     * Десериализует массив байтов в объект конфигурации.
     * Использует кодировку UTF-8 при чтении строковых данных из входных байтов.
     *
     * @param bytes Массив байтов, содержащий данные конфигурации в кодировке UTF-8
     * @throws IOException Если происходит ошибка при чтении или десериализации массива байтов
     */
    public abstract void readFromBytes(byte[] bytes) throws IOException;

    /**
     * Сериализует объект конфигурации в массив байтов.
     * Использует кодировку UTF-8 при преобразовании строковых данных в байты.
     *
     * @return Массив байтов, содержащий данные конфигурации в кодировке UTF-8
     */
    public abstract byte[] toBytes();

    /**
     * Заменяет значения полей текущего объекта конфигурации значениями из другого объекта.
     *
     * @param config Объект конфигурации, из которого копируются значения полей
     */
    protected void replaceFields(Config config) {
        for (Field field : config.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Field configField = this.getClass().getDeclaredField(field.getName());
                configField.setAccessible(true);
                configField.set(this, field.get(config));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                IOTMod.getInstance().getLogger().error("Failed to replace {} field in {}!",
                        field.getName(),
                        this.path,
                        e);
            }
        }
    }

    /**
     * Записывает конфигурацию на диск.
     * Создает необходимые директории, если они не существуют.
     *
     * @throws IOException Если происходит ошибка при записи файла
     */
    public void writeToDisk() throws IOException {
        if (!Files.exists(this.path.getParent())) {
            Files.createDirectories(this.path.getParent());
        }

        Files.write(this.path, this.toBytes());
    }

    /**
     * Безопасно записывает конфигурацию на диск.
     * В случае ошибки записи логирует её без выброса исключения.
     */
    public void writeToDiskSafe() {
        try {
            this.writeToDisk();
        } catch (IOException e) {
            IOTMod.getInstance().getLogger().error("Error writing {} to disk", this.path, e);
        }
    }

    /**
     * Читает конфигурацию с диска.
     * Если файл не существует, создает его с текущей конфигурацией.
     *
     * @throws IOException Если происходит ошибка при чтении файла
     */
    public void readFromDisk() throws IOException {
        if (!Files.exists(this.path)) this.writeToDisk();
        this.readFromBytes(Files.readAllBytes(this.path));
    }

    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.dir")).resolve("config");

    public static Path defaultConfigPath() {
        return CONFIG_DIR;
    }

}
