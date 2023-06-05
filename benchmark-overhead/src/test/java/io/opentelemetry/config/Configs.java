/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.config;

import io.opentelemetry.agents.Agent;
import java.util.Arrays;
import java.util.stream.Stream;

import static io.opentelemetry.agents.Agent.OTEL_LATEST;

/** Defines all test configurations */
public enum Configs {
  RELEASE(
      TestConfig.builder()
          .name("release")
          .description("compares no agent, latest stable, and latest snapshot agents")
          .withAgents(Agent.NONE, Agent.LATEST_RELEASE, Agent.LATEST_SNAPSHOT)
//          .withAgents(Agent.NONE, Agent.LATEST_RELEASE, Agent.LATEST_RELEASE_DEFAULT_DISABLED, Agent.LATEST_RELEASE_JDBC_ONLY)
          .withAgents(
              Agent.NONE,
              Agent.LATEST_RELEASE,
              new Agent("latest-gzip", "latest gzip", OTEL_LATEST, Arrays.asList(
//                  "-Dotel.javaagent.debug=true",
                  "-Dotel.instrumentation.experimental.span-suppression-strategy=span-kind",
                  "-Dotel.exporter.otlp.compression=gzip"
                )
              ),
              new Agent("latest-gzip-disabled", "latest gzip default-disabled", OTEL_LATEST, Arrays.asList(
//                  "-Dotel.javaagent.debug=true",
                  "-Dotel.instrumentation.experimental.span-suppression-strategy=span-kind",
                  "-Dotel.exporter.otlp.compression=gzip",
                  "-Dotel.instrumentation.common.default-enabled=false"
                )
              ),
              new Agent("latest-gzip-jdbc", "latest gzip default-disabled jdbc only", OTEL_LATEST, Arrays.asList(
//                  "-Dotel.javaagent.debug=true",
                  "-Dotel.instrumentation.experimental.span-suppression-strategy=span-kind",
                  "-Dotel.exporter.otlp.compression=gzip",
                  "-Dotel.instrumentation.common.default-enabled=false",
                  "-Dotel.instrumentation.jdbc.enabled=true"
                  )
              ),
              new Agent("latest-gzip-jdbc-2", "latest gzip default-disabled jdbc only", OTEL_LATEST, Arrays.asList(
//                  "-Dotel.javaagent.debug=true",
      "-Dotel.instrumentation.experimental.span-suppression-strategy=span-kind",
                  "-Dotel.exporter.otlp.compression=gzip",
                  "-Dotel.instrumentation.common.default-enabled=false",
                  "-Dotel.instrumentation.jdbc.enabled=true",
                  "-Dotel.experimental.resource.disabled-keys=host.arch,os.description,process.executable.path,process.runtime.description,process.runtime.version,telemetry.auto.version,telemetry.sdk.name,telemetry.sdk.version"
  )
              )
          )
          .warmupSeconds(60)
          .build());

  //none
  //latest
  //latest gzip
  //latest gzip default-disabled
  //latest gzip default-disabled + jdbc

  public final TestConfig config;

  public static Stream<TestConfig> all() {
    return Arrays.stream(Configs.values()).map(x -> x.config);
  }

  Configs(TestConfig config) {
    this.config = config;
  }
}
