package com.reproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.net.HttpURLConnection;

/**
 *
 */
public class ServerVerticle extends AbstractVerticle {

  public void start() {
    // Create HTTP Server handling GET requests
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route("/path").method(HttpMethod.GET).handler(routingContext -> {
      try {
        String absoluteURI = routingContext.request().absoluteURI();
        System.out.println("Server: absoluteURI = " + absoluteURI);
      } finally {
        if (!routingContext.response().ended()) {
          routingContext.response().end();
        }
      }
    });

    // Catch all
    router.routeWithRegex(".*").handler(routingContext -> {
      routingContext.response().setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
      routingContext.response().end();
    });

    server.requestHandler(router::accept).listen(8080);
  }
}
