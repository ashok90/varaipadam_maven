package com.iu.sqlnosql.verticle;

import com.iu.sqlnosql.constants.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.Neo4jException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

        String query = "MATCH (n:Jar) RETURN count(*);";
        StatementResult sr;
        List<Record> records;
        try ( Session session = driver.session() ) {
            sr = session.run(query);
            records = sr.list();
            message.reply("success");
        }
        message.fail(500, "db error");
    }
}
