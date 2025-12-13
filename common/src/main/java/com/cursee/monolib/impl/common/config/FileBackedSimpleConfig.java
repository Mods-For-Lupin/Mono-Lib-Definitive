package com.cursee.monolib.impl.common.config;

import java.io.*;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * A SimpleConfig that is backed by an internal resource file.
 * <br />
 * If the config file doesn't exist, it will be created from the internal resource.
 * If the config file exists, it will be loaded normally.
 * <br />
 * Handles directory creation and resource copying automatically.
 */
public class FileBackedSimpleConfig {

  private final SimpleConfig config;
  private final String configFilePath;
  private boolean isValid = false;

  /**
   * Creates or loads a configuration file backed by an internal resource.
   *
   * @param configFilePath The path where the config file should be stored (e.g., "config/mymod-common.toml")
   * @param internalResourcePath The classpath resource path (e.g., "assets/mymod-common.toml")
   * @param logger Optional logger for status messages (can be null for silent operation)
   */
  public FileBackedSimpleConfig(ClassLoader classLoader, String configFilePath, String internalResourcePath, Consumer<String> logger) {
    this.configFilePath = configFilePath;
    this.config = initializeConfig(classLoader, configFilePath, internalResourcePath, logger);
  }

  /**
   * Creates or loads a configuration file backed by an internal resource (silent mode).
   */
  public FileBackedSimpleConfig(ClassLoader classLoader, String configFilePath, String internalResourcePath) {
    this(classLoader, configFilePath, internalResourcePath, null);
  }

  /**
   * Creates or loads a configuration file backed by an internal resource.
   *
   * @param configFile The File where the config should be stored
   * @param internalResourcePath The classpath resource path
   * @param logger Optional logger for status messages
   */
  public FileBackedSimpleConfig(ClassLoader classLoader, File configFile, String internalResourcePath, Consumer<String> logger) {
    this(classLoader, configFile.getAbsolutePath(), internalResourcePath, logger);
  }

  /**
   * Creates or loads a configuration file backed by an internal resource (silent mode).
   */
  public FileBackedSimpleConfig(ClassLoader classLoader, File configFile, String internalResourcePath) {
    this(classLoader, configFile.getAbsolutePath(), internalResourcePath, null);
  }

  /**
   * Creates or loads a configuration file backed by an internal resource.
   *
   * @param configPath The Path where the config should be stored
   * @param internalResourcePath The classpath resource path
   * @param logger Optional logger for status messages
   */
  public FileBackedSimpleConfig(ClassLoader classLoader, Path configPath, String internalResourcePath, Consumer<String> logger) {
    this(classLoader, configPath.toString(), internalResourcePath, logger);
  }

  /**
   * Creates or loads a configuration file backed by an internal resource (silent mode).
   */
  public FileBackedSimpleConfig(ClassLoader classLoader, Path configPath, String internalResourcePath) {
    this(classLoader, configPath.toString(), internalResourcePath, null);
  }

  private SimpleConfig initializeConfig(ClassLoader classLoader, String configFilePath, String internalResourcePath, Consumer<String> logger) {
    File configFile = new File(configFilePath);
    File configDirectory = configFile.getParentFile();

    // Create config directory if needed
    if (configDirectory != null && !configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      log(logger, "Failed to create config directory: " + configDirectory.getAbsolutePath());
      return createEmptyConfig();
    }

    // If config file doesn't exist, copy from internal resource
    if (!configFile.exists()) {
      if (!copyFromResource(classLoader, configFile, internalResourcePath, logger)) {
        return createEmptyConfig();
      }
      log(logger, "Created new configuration file: " + configFilePath);
    } else {
      log(logger, "Reading configuration file: " + configFilePath);
    }

    // Load the config file
    try {
      this.isValid = true;
      return new SimpleConfig(configFilePath);
    } catch (IOException e) {
      log(logger, "Failed to read " + configFilePath + ": " + e.getMessage());
      return createEmptyConfig();
    }
  }

  private boolean copyFromResource(ClassLoader classLoader, File destination, String resourcePath, Consumer<String> logger) {
    try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        log(logger, "Failed to find internal resource: " + resourcePath);
        return false;
      }

      try (BufferedInputStream bis = new BufferedInputStream(inputStream);
          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination))) {

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
          bos.write(buffer, 0, bytesRead);
        }
      }
      return true;
    } catch (IOException e) {
      log(logger, "Failed to copy resource " + resourcePath + ": " + e.getMessage());
      return false;
    }
  }

  private SimpleConfig createEmptyConfig() {
    // Create an in-memory empty config by using a temporary file
    try {
      File tempFile = File.createTempFile("empty_config", ".tmp");
      tempFile.deleteOnExit();
      return new SimpleConfig(tempFile);
    } catch (IOException e) {
      // This should rarely happen, but we need to return something
      throw new RuntimeException("Failed to create fallback config", e);
    }
  }

  private void log(Consumer<String> logger, String message) {
    if (logger != null) {
      logger.accept(message);
    }
  }

  // Delegation methods to SimpleConfig

  public boolean getBoolean(String key, boolean defaultValue) {
    return config.getBoolean(key, defaultValue);
  }

  public byte getByte(String key, byte defaultValue) {
    return config.getByte(key, defaultValue);
  }

  public int getInteger(String key, int defaultValue) {
    return config.getInteger(key, defaultValue);
  }

  public short getShort(String key, short defaultValue) {
    return config.getShort(key, defaultValue);
  }

  public long getLong(String key, long defaultValue) {
    return config.getLong(key, defaultValue);
  }

  public float getFloat(String key, float defaultValue) {
    return config.getFloat(key, defaultValue);
  }

  public char getCharacter(String key, char defaultValue) {
    return config.getCharacter(key, defaultValue);
  }

  public String getString(String key, String defaultValue) {
    return config.getString(key, defaultValue);
  }

  /**
   * @return true if the config was successfully loaded or created
   */
  public boolean isValid() {
    return isValid;
  }

  /**
   * @return the path to the config file
   */
  public String getConfigFilePath() {
    return configFilePath;
  }
}