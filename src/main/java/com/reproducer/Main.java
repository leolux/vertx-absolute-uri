package com.reproducer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.Match;
import io.vertx.ext.dropwizard.MatchType;
import io.vertx.ext.dropwizard.MetricsService;

public class Main {

  public static String host = null;

  public static void main(String[] args) {
    try {
      host = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    // Enable Metrics
    VertxOptions options = new VertxOptions();
    DropwizardMetricsOptions metricsOptions = new DropwizardMetricsOptions();
    metricsOptions.setEnabled(true);
    metricsOptions.addMonitoredHttpServerUri(new Match().setValue("/*").setType(MatchType.REGEX));
    options.setMetricsOptions(metricsOptions);

    // Normal mode
    Vertx vertx = Vertx.vertx(options);
    deployVerticles(vertx);

    testHttpServerMetrics(vertx);

    // Cluster mode
    // VertxOptions options = new VertxOptions();
    // options.setClustered(true);
    // options.setClusterManager(new HazelcastClusterManager());
    // Vertx.clusteredVertx(options, vertxHandler -> {
    // if (vertxHandler.succeeded()) {
    // Vertx vertx = vertxHandler.result();
    // deployVerticles(vertx);
    // }
    // });
  }

  private static void testHttpServerMetrics(Vertx vertx) {
    vertx.setTimer(2000, l->{
      System.out.println("Test Http Server Metrics...");
      MetricsService metricsService = MetricsService.create(vertx);
      JsonObject metrics = metricsService.getMetricsSnapshot(vertx);
      boolean contains0000 = metrics.getValue("vertx.http.servers.0.0.0.0:8080.get-requests") != null;
      boolean containsHostname = metrics.getValue("vertx.http.servers." + host + ":8080.get-requests") != null;
      System.out.println("HTTP Server Metrics contains 0.0.0.0: " + contains0000);
      System.out.println("HTTP Server Metrics contains Hostname " + host + ": " + containsHostname);
    });
  }

  private static void deployVerticles(Vertx vertx) {
    vertx.deployVerticle(new ServerVerticle());
    vertx.deployVerticle(new ClientVerticle());
    System.out.println("Wait a second...");
  }
}
