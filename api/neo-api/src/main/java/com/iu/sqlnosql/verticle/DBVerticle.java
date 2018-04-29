package com.iu.sqlnosql.verticle;

import com.iu.sqlnosql.constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.Neo4jException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;


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
                getDependencies(message);
            });
        } else {
            logger.error("DBVerticle deployment error");
        }

    }


    void getDependencies(final Message<Object> message) {

        logger.info("Recieved message {}", message.body());

        JsonObject jsonObject = (JsonObject) message.body();

        logger.info(jsonObject.getString("name"));

        StatementResult sr;
        try ( Session session = driver.session() ) {
            logger.info(Constants.QUERY);
            sr = session.run(Constants.QUERY, Values.parameters( "name", "JetS3t" ));
            while ( sr.hasNext() )
            {
                Record record = sr.next();
                Gson gson = new Gson();
                message.reply(gson.toJson(record.asMap()));
            }

        }
        message.fail(500, "db error");
    }
}
