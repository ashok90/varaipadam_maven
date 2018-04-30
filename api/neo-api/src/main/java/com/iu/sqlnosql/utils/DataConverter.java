package com.iu.sqlnosql.utils;

import com.google.gson.*;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

public class DataConverter {


    public static String GenerateLinkedJson(StatementResult sr, String searchString) {

        JsonElement parentId = null;

        while ( sr.hasNext() )
        {
            Record record = sr.next();

            JsonObject returnObject = new JsonObject();
            JsonArray retNodeArrays = new JsonArray();
            Gson gson = new Gson();

            JsonElement jelement = new JsonParser().parse(gson.toJson(record.asMap()));

            JsonArray jarray = jelement.getAsJsonObject().getAsJsonArray("nodes");

            for(JsonElement element : jarray) {

                JsonObject tempObject = new JsonObject();
                JsonObject elemObj = element.getAsJsonObject();
                JsonObject propObj = elemObj.getAsJsonObject("properties");

                if(searchString.equalsIgnoreCase(propObj.get("name").getAsJsonObject().get("val").getAsString())) {
                    continue;
                }

                tempObject.add("group", elemObj.get("id"));
                tempObject.add("name", propObj.get("name").getAsJsonObject().get("val"));
                tempObject.add("group_id", propObj.get("group_id").getAsJsonObject().get("val"));
                tempObject.add("artifact_id", propObj.get("artifact_id").getAsJsonObject().get("val"));
                tempObject.add("version", propObj.get("version").getAsJsonObject().get("val"));
                tempObject.add("no_of_dependencies", propObj.get("no_of_dependencies").getAsJsonObject().get("val"));
                retNodeArrays.add(tempObject);
            }

            /*JsonArray retLinkArrays = new JsonArray();

            for(JsonElement element: retNodeArrays) {
                if(parentId == element.getAsJsonObject().get("group")) {
                    continue;
                }

                JsonObject tempObject = new JsonObject();
                tempObject.add("source",  parentId);
                tempObject.add("target", element.getAsJsonObject().get("group"));
                tempObject.addProperty("weight",  1);
                retLinkArrays.add(tempObject);
            }*/

            returnObject.add("nodes", retNodeArrays);
            //returnObject.add("links", retLinkArrays);

            return gson.toJson(returnObject);
        }

        return "";
    }

}
