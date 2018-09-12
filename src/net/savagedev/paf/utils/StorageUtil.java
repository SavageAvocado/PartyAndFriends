package net.savagedev.paf.utils;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.savagedev.paf.PartyAndFriends;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class StorageUtil {
    private ConfigurationProvider configurationProvider;
    private Map<UUID, Configuration> configurations;
    private Configuration configuration;
    private PartyAndFriends plugin;

    public StorageUtil(PartyAndFriends plugin) {
        this.configurationProvider = YamlConfiguration.getProvider(YamlConfiguration.class);
        this.configurations = new HashMap<>();
        this.plugin = plugin;

        this.startCleanupThread();
    }

    private void startCleanupThread() {
        this.plugin.getProxy().getScheduler().schedule(this.plugin, new Runnable() {
            @Override
            public void run() {
                if (configurations.isEmpty())
                    return;

                for (UUID uuid : configurations.keySet())
                    if (!plugin.getUserManager().playerIsOnline(uuid))
                        unCacheFile(uuid);
            }
        }, 0L, 5L, TimeUnit.MINUTES);
    }

    public void saveDefaultConfig() {
        File file = new File(this.plugin.getDataFolder(), "config.yml");
        if (file.exists()) return;

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream configInputStream = this.plugin.getResourceAsStream("config.yml")) {
            OutputStream configOutPutStream = new FileOutputStream(file);
            ByteStreams.copy(configInputStream, configOutPutStream);
        } catch (Exception ignored) {
        }
    }

    public void createFile(UUID uuid) {
        File file = new File(this.plugin.getDataFolder(), "storage/" + uuid.toString() + ".yml");
        if (file.exists()) return;

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.configurationProvider.save(this.getConfiguration(), new File(this.plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(UUID uuid) {
        try {
            this.configurationProvider.save(this.getStorageFile(uuid), new File(this.plugin.getDataFolder(), "storage/" + uuid.toString() + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            this.configuration = this.configurationProvider.load(new File(this.plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadFile(UUID uuid) {
        try {
            this.configurations.put(uuid, this.configurationProvider.load(new File(this.plugin.getDataFolder(), "storage/" + uuid.toString() + ".yml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unCacheFile(UUID uuid) {
        this.saveFile(uuid);
        this.configurations.remove(uuid, this.configurations.get(uuid));
    }

    public void setDefaults(UUID uuid, KeyValueSet... keyValueSets) {
        for (KeyValueSet keyValueSet : keyValueSets)
            this.getStorageFile(uuid).set(keyValueSet.getKey(), keyValueSet.getValue());
    }

    public boolean fileExists(UUID uuid) {
        return new File(this.plugin.getDataFolder(), "storage/" + uuid.toString() + ".yml").exists();
    }

    public Configuration getConfiguration() {
        if (this.configuration == null)
            this.reload();

        return this.configuration;
    }

    public Configuration getStorageFile(UUID uuid) {
        if (!this.configurations.containsKey(uuid)) {
            try {
                this.configurations.put(uuid, this.configurationProvider.load(new File(this.plugin.getDataFolder(), "storage/" + uuid.toString() + ".yml")));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return this.configurations.get(uuid);
    }
}
