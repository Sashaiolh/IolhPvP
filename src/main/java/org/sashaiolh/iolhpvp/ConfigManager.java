package org.sashaiolh.iolhpvp;


import java.io.*;
import java.util.*;

public class ConfigManager {
    private static String CONFIG_PATH = "";
    private static Properties properties;

    public ConfigManager(String path) {
        CONFIG_PATH = path;
        properties = new Properties(); // Всегда создаем новый экземпляр Properties
        loadConfig(); // Загружаем существующую конфигурацию
        createDefaultConfig(); // Добавляем недостающие значения
    }

    private void loadConfig() {
        try (InputStream inputStream = new FileInputStream(CONFIG_PATH)) {
            properties.load(new InputStreamReader(inputStream, "UTF-8")); // Загружаем конфигурацию
        } catch (IOException e) {
            createDefaultConfig(); // Создаем новую конфигурацию, если файл отсутствует
            loadConfig(); // Повторная загрузка после создания файла
        }
    }

    private static void saveConfig() {
        try {
            File configFile = new File(CONFIG_PATH);
            configFile.getParentFile().mkdirs();
            try (OutputStream outputStream = new FileOutputStream(CONFIG_PATH)) {
                properties.store(new OutputStreamWriter(outputStream, "UTF-8"), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultConfig() {
        Map<String, String> defaultProperties = new HashMap<>();

        defaultProperties.put("messagePrefix", "§8[§6EnigmaRPG§8]§r ");
        defaultProperties.put("reloadConfigMessage", "§aКонфиг был перезагружен!");
        defaultProperties.put("pvpModeOn", "§cВы вошли в режим PvP! Не выходите из сервера!");
//        defaultProperties.put("pvpModeOnForAttacker", "§cВы в боевом режиме!");
//        defaultProperties.put("pvpModeOnForTarget", "§cВы в боевом режиме!");
        defaultProperties.put("turnOffPvpMode", "§6У вас включена защита! Если хотите её отключить - пропишите §6/pvp§c.");
        defaultProperties.put("youAreProtected", "");
        defaultProperties.put("playerIsProtected", "§cУ игрока включена защита от PvP!");
        defaultProperties.put("timerBarLabel", "Таймер PvP");
        defaultProperties.put("onCommandMessage", "§cВо время боя вы не можете использовать команды!");
        defaultProperties.put("pvpModeEndMessage", "§6Режим PvP окончен.");
        defaultProperties.put("pvpProtectionOn", "§6Защита от PvP §aвключена§6!");
        defaultProperties.put("pvpProtectionOff", "§6Защита от PvP §cвыключена§6!");

        synchronized (properties) {
            for (Map.Entry<String, String> property : defaultProperties.entrySet()) {
                if (properties.getProperty(property.getKey()) == null) {
                    properties.setProperty(property.getKey(), property.getValue()); // Добавляем недостающие значения
                }
            }
        }

        saveConfig(); // Сохраняем конфигурацию после добавления новых значений
    }

    public Set<String> getAllKeys() {
        return properties.stringPropertyNames();
    }

    public String getConfig(String key) {
        return properties.getProperty(key);
    }

    public void setConfig(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }
}