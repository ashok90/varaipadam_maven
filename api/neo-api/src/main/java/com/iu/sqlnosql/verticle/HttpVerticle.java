package com.iu.sqlnosql.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.Neo4jException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

import java.util.List;


public class HttpVerticle extends AbstractVerticle {

    static final String Boltx_port_key = "boltx.port";
    static final String Boltx_bolt_uri_key = "bolt://localhost:7687";
    static final String Boltx_neo4j_username_key = "neo4j";
    static final String Boltx_neo4j_password_key = "neo4j";

    protected Driver driver;

    private HttpServer server;

    protected boolean setupBoltDriver() {
        try {
            System.out.println("in setupBoltDriver");
            driver = GraphDatabase.driver( Boltx_bolt_uri_key,
                    AuthTokens.basic( Boltx_neo4j_username_key, Boltx_neo4j_password_key ) );
            System.out.println("completed setupBoltDriver");
        } catch (Neo4jException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }


    void getStatus(RoutingContext rc) {
        rc.response().setStatusCode(200)
                .putHeader("content-type", "text/html; charset=utf-8")
                .end("service is up and running");
    }

    void getCount(RoutingContext rc) {
        JsonObject obj = new JsonObject();
        String query = "MATCH (n:Jar) RETURN count(*);";
        StatementResult sr;
        List<Record> records;
        try ( Session session = driver.session() ) {
            sr = session.run(query);
            records = sr.list();
        }
        rc.response()
                .putHeader("content-type", "text/plain")
                .end(records.toString());
    }

    @Override
    public void start(Future<Void> startFuture) {

        Router router = Router.router(vertx);

        router.route("/status").handler(this::getStatus);

        router.route("/count").handler(this::getCount);

        if(setupBoltDriver()) {
            server = vertx.createHttpServer();
            server.requestHandler(router::accept).listen(9090, res -> {
                if (res.succeeded()) {
                    System.out.println("server started");
                    startFuture.complete();
                } else {
                    startFuture.fail(res.cause());
                }
            });
        }
    }


}
