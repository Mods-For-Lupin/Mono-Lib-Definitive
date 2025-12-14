package com.cursee.monolib.impl.common.sailing.warden;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <a
 * href="https://github.com/Mycelium-Mod-Network/Warden/blob/main/src/main/java/network/myceliummod/warden/ZoneIdentifier.java">Warden
 * ZoneIdentifier.java</a>
 */
public class ZoneIdentifier {

  private static final String ZONE_IDENTIFIER = ":Zone.Identifier";
  private static final String REFERRER_URL = "ReferrerUrl=";
  private static final String HOST_URL = "HostUrl=";

  private final String host;

  private final String referrer;

  private ZoneIdentifier(String host, String referrer) {

    this.host = host;
    this.referrer = referrer;
  }

  public static ZoneIdentifier of(File file) {

    final File zoneFile = new File(file.getAbsolutePath() + ZONE_IDENTIFIER);

    if (zoneFile.exists()) {

      try (BufferedReader reader = new BufferedReader(new FileReader(zoneFile))) {

        String host = null;
        String referrer = null;
        String line;

        while ((line = reader.readLine()) != null && (referrer == null || host == null)) {
          if (line.startsWith(REFERRER_URL)) {
            referrer = stripInfo(line.substring(REFERRER_URL.length()));
          } else if (line.startsWith(HOST_URL)) {
            host = stripInfo(line.substring(HOST_URL.length()));
          }
        }

        return new ZoneIdentifier(host, referrer);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return null;
  }

  private static String stripInfo(String urlString) {

    int startIdx = urlString.indexOf("://");

    if (startIdx != -1) {

      startIdx += 3;
      int endIdx = urlString.indexOf('/', startIdx);

      if (endIdx != -1) {
        return urlString.substring(startIdx, endIdx);
      }
    }

    return urlString;
  }

  public String getHost() {

    return this.host;
  }

  public String getReferrer() {

    return this.referrer;
  }

  @Override
  public String toString() {

    return "ZoneIdentifier{" + "host='" + host + '\'' + ", referrer='" + referrer + '\'' + '}';
  }
}

