package com.cursee.monolib.impl.common.config;

import com.cursee.monolib.MonoLib;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The most basic a configuration can be! Reads all lines for key=value pairs, stores the original String, and parses when needed.
 * <br />
 * Only supports primitives and String; no arrays/lists/maps/etc.
 * <br />
 * May silently fail. failure to parse an entry will return its default value.
 */
public class SimpleConfig {

  private final Map<String, String> entries = new LinkedHashMap<>();

  public SimpleConfig(File file) throws IOException {
    this(file.toURI());
  }

  public SimpleConfig(Path path) throws IOException {
    this(path.toString());
  }

  public SimpleConfig(URI path) throws IOException {
    this(path.toString());
  }

  public SimpleConfig(String path) throws IOException {
    this.load(path);
  }

  /// @return "the previous value associated with `key`, or null if there was no mapping for `key`" {@link Map}
  public String addEntry(String key, String value) {
    return entries.put(key, value);
  }

  /// Attempts to read the given String as a file using {@link FileReader}
  private void load(String path) throws IOException {

    if (MonoLib.debugging) {
      MonoLib.LOG.info("SimpleConfig#load called with String path {}", path);
    }

    // create a new buffered reader and attempt to read the given file path into memory
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

      // to hold the content we are reading
      String line;

      // while we are able to read a line from the file
      while ((line = reader.readLine()) != null) {

        // remove leading and trailing whitespace
        line = line.trim();

        // check for empty lines and comments (lines beginning with # or //)
        if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
          continue; // skip this line
        }

        // split the line into two parts (i.e. "key=value" -> ["key", "value"])
        String[] parts = line.split("=", 2);

        // sanity check... do we have 2 parts ?
        if (parts.length == 2) {
          String key = parts[0].trim(); // trims any extra spacing (i.e. key   =value -> key)
          String value = parts[1].trim(); // trims any extra spacing (i.e. key=    value -> value)
          entries.put(key, value);
        }
      }
    }
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return Boolean.parseBoolean(value);
  }

  public byte getByte(String key, byte defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return Byte.parseByte(value);
  }

  public int getInteger(String key, int defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return Integer.parseInt(value);
  }

  public short getShort(String key, short defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return Short.parseShort(value);
  }

  public long getLong(String key, long defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return Long.parseLong(value);
  }

  public float getFloat(String key, float defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return Float.parseFloat(value);
  }

  public char getCharacter(String key, char defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return value.length() == 1 ? value.charAt(0) : defaultValue;
  }

  public String getString(String key, String defaultValue) {
    String value = entries.get(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
