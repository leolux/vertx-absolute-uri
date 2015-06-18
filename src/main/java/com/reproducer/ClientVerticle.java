package com.reproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;

/**
 *
 */
public class ClientVerticle extends AbstractVerticle {

  public void start() {
    // Give the server some time to start (quick and dirty)
    vertx.setTimer(1000, l -> {
      HttpClient client = vertx.createHttpClient();

      // Is this non-blocking?
        String path = "/path";
        int port = 8080;
        System.out.println("Client: GET Request-URI = http://" + Main.host + ":" + port + path);
        client.getNow(port, Main.host, path, resp -> {
          System.out.println("Received response with status code " + resp.statusCode());
        });

    });
  }

}
