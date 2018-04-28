package com.iu.sqlnosql.verticle;

import com.iu.sqlnosql.constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
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
                getCount(message);
            });
        } else {
            logger.error("DBVerticle deployment error");
        }

    }


    void getCount(final Message<Object> message) {

        logger.info("Recieved message {}", message.body());

        String query = "MATCH (a:Artifacts {name:'JetS3t'})-[:DEPENDENT_ON]->(n)\n" +
                "WITH a + collect(n) as nodes\n" +
                "RETURN nodes";
        StatementResult sr;
        try ( Session session = driver.session() ) {
            logger.info(query);
            sr = session.run(query);
            while ( sr.hasNext() )
            {
                Record record = sr.next();
                Gson gson = new Gson();
                System.out.println(gson.toJson(record.asMap()));
                message.reply(gson.toJson(record.asMap()));
            }

        }
        message.fail(500, "db error");
    }
}
