package com.iu.sqlnosql.verticle;

import com.iu.sqlnosql.constants.Constants;
import com.iu.sqlnosql.utils.DataConverter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.Neo4jException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class DBVerticle extends AbstractVerticle {

    final Logger logger = LoggerFactory.getLogger(DBVerticle.class);

    private Driver driver;

    private boolean setupBoltDriver() {
        try {
            driver = GraphDatabase.driver( Constants.Boltx_bolt_uri_key,
                    AuthTokens.basic( Constants.Boltx_neo4j_username_key, Constants.Boltx_neo4j_password_key ) );
        } catch (Neo4jException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    @Override
    public void start(Future<Void> startFuture) {

        logger.info("Deploying db verticle");

        if(setupBoltDriver()) {
            logger.info("Deploying db verticle completed");
            vertx.eventBus().consumer(Constants.DBSERVICE_ADDRESS, message -> {
                handleMessage(message);
            });
        } else {
            logger.error("DBVerticle deployment error");
        }

    }

    void handleMessage(final Message<Object> message) {

        JsonObject jsonObject = (JsonObject) message.body();

        switch(jsonObject.getString("type")) {
            case "getDependency":
                getDependencies(message);
                break;
            case "addArtifact":
                addArtifact(message);
                break;
            case "deleteArtifact":
                deleteArtifact(message);
                break;
            case "linkArtifacts":
                linkArtifacts(message);
                break;
            default:
                logger.error("invalid type");
                break;
        }

    }


    void getDependencies(final Message<Object> message) {

        logger.info("Received message {}", message.body());

        JsonObject jsonObject = (JsonObject) message.body();

        String name = jsonObject.getString("name");

        logger.info(name);

        StatementResult sr;
        try ( Session session = driver.session() ) {
            logger.info(Constants.SEARCH_QUERY);
            sr = session.run(Constants.SEARCH_QUERY, Values.parameters( "name",  name));

            /*while ( sr.hasNext() )
            {
                Record record = sr.next();
                Gson gson = new Gson();
                message.reply(gson.toJson(record.asMap()));
            }*/

            message.reply(DataConverter.GenerateLinkedJson(sr, name));

        }
        message.fail(500, "db error");
    }

    void addArtifact(final Message<Object> message) {

        logger.info("Received message {}", message.body());

        JsonObject jsonObject = (JsonObject) message.body();

        logger.info(jsonObject.getString("name"));

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", jsonObject.getString("name"));
        parameters.put("group_id", jsonObject.getString("groupId"));
        parameters.put("artifact_id", jsonObject.getString("artifactId"));
        parameters.put("version", jsonObject.getDouble("version"));
        parameters.put("no_of_dependencies", jsonObject.getDouble("noOfDependencies"));

        StatementResult sr;
        try ( Session session = driver.session() ) {
            logger.info(Constants.CREATE_QUERY);
            sr = session.run(Constants.CREATE_QUERY, parameters);
            System.out.println(sr.summary());
            message.reply("creation operation completed...");
        }
        message.fail(500, "create operation failed");
    }

    void deleteArtifact(final Message<Object> message) {

        logger.info("Received message {}", message.body());

        JsonObject jsonObject = (JsonObject) message.body();

        logger.info(jsonObject.getString("name"));

        StatementResult sr;
        try ( Session session = driver.session() ) {
            logger.info(Constants.DELETE_QUERY);
            sr = session.run(Constants.DELETE_QUERY, Values.parameters( "name",  jsonObject.getString("name")));
            System.out.println(sr.summary());
            message.reply("delete operation completed...");

        }
        message.fail(500, "delete operation failed");

    }

    void linkArtifacts(final Message<Object> message) {

        logger.info("Received message {}", message.body());

        JsonObject jsonObject = (JsonObject) message.body();

        logger.info(jsonObject.getString("name"));

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("child_jar", jsonObject.getString("childJar"));
        parameters.put("parent_jar", jsonObject.getString("parentJar"));

        StatementResult sr;
        try ( Session session = driver.session() ) {
            logger.info(Constants.LINK_QUERY);
            sr = session.run(Constants.LINK_QUERY, parameters);
            System.out.println(sr.summary());
            message.reply("artifact linked...");
        }
        message.fail(500, "artifact links failed");

    }
}
