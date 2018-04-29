package com.iu.sqlnosql.constants;

public final class Constants {

    public static final String DBSERVICE_ADDRESS = "neo.dbservice";
    public static final String Boltx_port_key = "boltx.port";
    public static final String Boltx_bolt_uri_key = "bolt://localhost:7687";
    public static final String Boltx_neo4j_username_key = "neo4j";
    public static final String Boltx_neo4j_password_key = "neo4j";
    public static final String SEARCH_QUERY = "MATCH (a:Artifacts {name: {name}})-[:DEPENDENT_ON]->(n)\n" +
            "WITH a + collect(n) as nodes\n" +
            "RETURN nodes";
    public static final String CREATE_QUERY = "CREATE (:Artifacts {name: {name}, group_id: {group_id}, " +
            "artifact_id: {artifact_id}, version: {version}, " +
            "no_of_dependencies: {no_of_dependencies}});";
    public static final String DELETE_QUERY = "MATCH (n:Artifacts {name: {name}}) DETACH DELETE n";
    public static final String LINK_QUERY = "MATCH (child:Artifacts { name: {child_jar}}),  " +
            "(parent:Artifacts { name: {parent_jar}})\n" +
            "CREATE (child)-[:DEPENDENT_ON]->(parent)";

}
