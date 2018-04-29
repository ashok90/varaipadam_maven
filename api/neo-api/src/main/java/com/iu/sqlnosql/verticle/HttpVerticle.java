package com.iu.sqlnosql.verticle;

import com.iu.sqlnosql.constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.ext.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpVerticle extends AbstractVerticle {

    private HttpServer server;

    final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);

    private HTTPRequestValidationHandler searchHandler = HTTPRequestValidationHandler.create()
            .addQueryParam("name", ParameterType.GENERIC_STRING, true);

    private HTTPRequestValidationHandler createHandler = HTTPRequestValidationHandler.create()
            .addQueryParam("name", ParameterType.GENERIC_STRING, true)
            .addQueryParam("groupId", ParameterType.GENERIC_STRING, true)
            .addQueryParam("artifactId", ParameterType.GENERIC_STRING, true)
            .addQueryParam("version", ParameterType.DOUBLE, true)
            .addQueryParam("noOfDependencies", ParameterType.DOUBLE, true);

    private HTTPRequestValidationHandler deleteHandler = HTTPRequestValidationHandler.create()
            .addQueryParam("name", ParameterType.GENERIC_STRING, true);

    private HTTPRequestValidationHandler linkHandler = HTTPRequestValidationHandler.create()
            .addQueryParam("childJar", ParameterType.GENERIC_STRING, true)
            .addQueryParam("parentJar", ParameterType.GENERIC_STRING, true);

    @Override
    public void start(Future<Void> startFuture) {

        logger.info("Deploying http verticle");

        Router router = Router.router(vertx);

        router.route("/status").handler(this::getStatus);

        router.route("/get_dependencies")
                .handler(searchHandler)
                .handler(this::getDependencies)
                .failureHandler((routingContext) -> {
                    Throwable failure = routingContext.failure();
                    if (failure instanceof ValidationException) {
                        // Something went wrong during validation!
                        String validationErrorMessage = failure.getMessage();
                        routingContext.response().setStatusCode(500)
                                .putHeader("content-type", "text/html; charset=utf-8")
                                .putHeader("Access-Control-Allow-Origin", "*")
                                .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                                .end(validationErrorMessage);
                    }
                });

        router.route("/add_artifact")
                .handler(createHandler)
                .handler(this::addArtifact)
                .failureHandler((routingContext) -> {
                    Throwable failure = routingContext.failure();
                    if (failure instanceof ValidationException) {
                        // Something went wrong during validation!
                        String validationErrorMessage = failure.getMessage();
                        routingContext.response().setStatusCode(500)
                                .putHeader("content-type", "text/html; charset=utf-8")
                                .putHeader("Access-Control-Allow-Origin", "*")
                                .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                                .end(validationErrorMessage);
                    }
                });

        router.route("/delete_artifact")
                .handler(deleteHandler)
                .handler(this::deleteArtifact)
                .failureHandler((routingContext) -> {
                    Throwable failure = routingContext.failure();
                    if (failure instanceof ValidationException) {
                        // Something went wrong during validation!
                        String validationErrorMessage = failure.getMessage();
                        routingContext.response().setStatusCode(500)
                                .putHeader("content-type", "text/html; charset=utf-8")
                                .putHeader("Access-Control-Allow-Origin", "*")
                                .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                                .end(validationErrorMessage);
                    }
                });

        router.route("/link_artifacts")
                .handler(linkHandler)
                .handler(this::linkArtifacts)
                .failureHandler((routingContext) -> {
                    Throwable failure = routingContext.failure();
                    if (failure instanceof ValidationException) {
                        // Something went wrong during validation!
                        String validationErrorMessage = failure.getMessage();
                        routingContext.response().setStatusCode(500)
                                .putHeader("content-type", "text/html; charset=utf-8")
                                .putHeader("Access-Control-Allow-Origin", "*")
                                .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                                .end(validationErrorMessage);
                    }
                });

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
        logger.info("server is up and running");
        rc.response().setStatusCode(200)
                .putHeader("content-type", "text/html; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                .end("service is up and running");
    }

    void getDependencies(RoutingContext rc) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.put("type", "getDependency");

        RequestParameters params = rc.get("parsedParameters");

        jsonObject.put("name", params.queryParameter("name").getString());

        vertx.eventBus().send(Constants.DBSERVICE_ADDRESS, jsonObject, response -> {
            if (response.succeeded()) {
                rc.response().setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.result().body().toString());
            } else {
                rc.response().setStatusCode(500)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.cause().getMessage());
            }
        });

    }

    void addArtifact(RoutingContext rc) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.put("type", "addArtifact");

        RequestParameters params = rc.get("parsedParameters");

        jsonObject.put("name", params.queryParameter("name").getString());
        jsonObject.put("groupId", params.queryParameter("groupId").getString());
        jsonObject.put("artifactId", params.queryParameter("artifactId").getString());
        jsonObject.put("version", params.queryParameter("version").getDouble());
        jsonObject.put("noOfDependencies", params.queryParameter("noOfDependencies").getDouble());

        /*for (Map.Entry<String, String> entry : rc.queryParams().entries()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }*/

        vertx.eventBus().send(Constants.DBSERVICE_ADDRESS, jsonObject, response -> {
            if (response.succeeded()) {
                logger.info("processed add operation");
                rc.response().setStatusCode(200)
                        .putHeader("content-type", "text/html; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.result().body().toString());
            } else {
                rc.response().setStatusCode(500)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.cause().getMessage());
            }
        });

    }

    void deleteArtifact(RoutingContext rc) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.put("type", "deleteArtifact");

        RequestParameters params = rc.get("parsedParameters");

        jsonObject.put("name", params.queryParameter("name").getString());

        vertx.eventBus().send(Constants.DBSERVICE_ADDRESS, jsonObject, response -> {
            if (response.succeeded()) {
                logger.info("processed delete operation");
                rc.response().setStatusCode(200)
                        .putHeader("content-type", "text/html; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.result().body().toString());
            } else {
                rc.response().setStatusCode(500)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.cause().getMessage());
            }
        });

    }


    void linkArtifacts(RoutingContext rc) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.put("type", "linkArtifacts");

        RequestParameters params = rc.get("parsedParameters");

        jsonObject.put("childJar", params.queryParameter("childJar").getString());
        jsonObject.put("parentJar", params.queryParameter("parentJar").getString());


        vertx.eventBus().send(Constants.DBSERVICE_ADDRESS, jsonObject, response -> {
            if (response.succeeded()) {
                logger.info("processed link operation");
                rc.response().setStatusCode(200)
                        .putHeader("content-type", "text/html; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.result().body().toString());
            } else {
                rc.response().setStatusCode(500)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
                        .end(response.cause().getMessage());
            }
        });

    }



}
