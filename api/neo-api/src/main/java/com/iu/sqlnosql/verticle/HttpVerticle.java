package com.iu.sqlnosql.verticle;

import com.iu.sqlnosql.constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {

    private HttpServer server;

    final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);


    @Override
    public void start(Future<Void> startFuture) {

        logger.info("Deploying http verticle");

        Router router = Router.router(vertx);

        router.route("/status").handler(this::getStatus);

        router.route("/get_dependencies").handler(this::getDependencies);

        server = vertx.createHttpServer();
            server.requestHandler(router::accept).listen(9090, res -> {
                if (res.succeeded()) {
                    logger.info("Http server started");
                    startFuture.complete();
                } else {
                    startFuture.fail(res.cause());
                }
            });
    }

    void getStatus(RoutingContext rc) {
        rc.response().setStatusCode(200)
                .putHeader("content-type", "text/html; charset=utf-8")
                .end("service is up and running");
    }

    void getDependencies(RoutingContext rc) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.put("name", rc.queryParam("name").get(0));

        vertx.eventBus().send(Constants.DBSERVICE_ADDRESS, jsonObject, response -> {
            if (response.succeeded()) {
                rc.response().setStatusCode(200).end(response.result().body().toString());
            } else {
                rc.response().setStatusCode(500).end(response.cause().getMessage());
            }
        });

    }


}
