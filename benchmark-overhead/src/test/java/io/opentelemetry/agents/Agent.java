/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.agents;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Agent {

  public static final String OTEL_LATEST =
      "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar";

  public static final Agent NONE = new Agent("none", "no agent at all");
  public static final Agent LATEST_RELEASE =
      new Agent("latest", "latest mainstream release", OTEL_LATEST);
  public static final Agent LATEST_SNAPSHOT =
      new Agent("snapshot", "latest available snapshot version from main");



  public static final Agent LATEST_RELEASE_DEFAULT_DISABLED =
      new Agent(
          "latest-default-disabled",
          "latest mainstream release default disabled",
          OTEL_LATEST,
          Arrays.asList(
            "-Dotel.exporter.otlp.compression=gzip",
            "-Dotel.instrumentation.common.default-enabled=false"
      ));

  public static final Agent LATEST_RELEASE_JDBC_ONLY =
      new Agent("latest-jdbc-only",
          "latest mainstream release default disabled jdbc enabled",
          OTEL_LATEST,
          Arrays.asList(
            "-Dotel.exporter.otlp.compression=gzip",
            "-Dotel.instrumentation.common.default-enabled=false",
            "-Dotel.instrumentation.jdbc.enabled=true"
      ));

  private final String name;
  private final String description;
  private final URL url;
  private final List<String> additionalJvmArgs;

  public Agent(String name, String description) {
    this(name, description, null);
  }

  public Agent(String name, String description, String url) {
    this(name, description, url, Collections.emptyList());
  }

  public Agent(String name, String description, String url, List<String> additionalJvmArgs) {
    this.name = name;
    this.description = description;
    this.url = makeUrl(url);
    this.additionalJvmArgs = new ArrayList<>(additionalJvmArgs);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean hasUrl() {
    return url != null;
  }

  public URL getUrl() {
    return url;
  }

  public List<String> getAdditionalJvmArgs() {
    return Collections.unmodifiableList(additionalJvmArgs);
  }

  private static URL makeUrl(String url) {
    try {
      if (url == null) {
        return null;
      }
      return URI.create(url).toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error parsing url", e);
    }
  }
}
