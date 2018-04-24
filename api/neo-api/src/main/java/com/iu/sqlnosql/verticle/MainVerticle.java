package com.iu.sqlnosql.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        vertx.deployVerticle("com.iu.sqlnosql.verticle.HttpVerticle");
    }

}
