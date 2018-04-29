package com.iu.sqlnosql.constants;

public final class Constants {

    public static final String DBSERVICE_ADDRESS = "neo.dbservice";
    public static final String Boltx_port_key = "boltx.port";
    public static final String Boltx_bolt_uri_key = "bolt://localhost:7687";
    public static final String Boltx_neo4j_username_key = "neo4j";
    public static final String Boltx_neo4j_password_key = "neo4j";
    public static String QUERY = "MATCH (a:Artifacts {name: {name}})-[:DEPENDENT_ON]->(n)\n" +
            "WITH a + collect(n) as nodes\n" +
            "RETURN nodes";
}
